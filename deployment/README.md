# Catalogue Service — Deployment (AWS ECS Fargate)

Deploys `catalogue-service` as a container on AWS ECS Fargate, using the image in
Amazon ECR. This documents the exact steps used, including the issues hit along
the way and how they were resolved.

- Service port: **8082**
- ECR repo: `catalogue-service`
- ECS cluster: `theatre-cluster` (shared with the other services)
- Task definition file: `deployment/catalogue-taskdef.json`
- Region: `us-east-1`

> Environment note: this was done in an **AWS Academy Learner Lab** (`voclabs`
> role). App Runner is **blocked** there (`apprunner:*` denied), so ECS Fargate
> is used instead. The lab also resets between sessions, so treat everything
> below as repeatable from scratch.

---

## Prerequisites

- AWS CLI v2, authenticated with valid lab credentials.
- Docker Desktop running.
- Build machine is Apple Silicon (arm64) — this matters (see "Gotchas").
- **The same `JWT_SECRET` as identity-service** — catalogue validates tokens that
  identity-service signs, so the secret must match exactly.

Refresh credentials after any lab restart, then confirm:

```bash
aws sts get-caller-identity
```

---

## One-time setup (per fresh lab session)

```bash
AWS_REGION=us-east-1
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ECR_REPO=catalogue-service
IMAGE_URI="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest"

# ECR repository
aws ecr create-repository --repository-name "$ECR_REPO" --region "$AWS_REGION" 2>/dev/null

# ECS cluster (shared; safe to run even if it already exists)
aws ecs create-cluster --cluster-name theatre-cluster --region "$AWS_REGION" 2>/dev/null

# CloudWatch log group (referenced by the task definition)
aws logs create-log-group --log-group-name /ecs/catalogue-service --region "$AWS_REGION" 2>/dev/null
```

---

## Step 1 — Build the JAR on the host

The Dockerfile is **runtime-only**: it copies a pre-built fat JAR rather than
running Maven inside the container. Build the JAR first (run from the
`catalogue-service` directory):

```bash
./mvnw -DskipTests clean package
ls target/*.jar   # expect catalogue-service-0.0.1-SNAPSHOT.jar
```

## Step 2 — Build the image for linux/amd64 and push to ECR

Fargate runs on **x86_64**, so the image must be built for `linux/amd64` even
though the build machine is arm64.

```bash
aws ecr get-login-password --region "$AWS_REGION" \
  | docker login --username AWS --password-stdin "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

docker buildx build --platform linux/amd64 -t "$IMAGE_URI" --push .

# Verify the pushed image is amd64
docker buildx imagetools inspect "$IMAGE_URI" | grep -i platform   # expect linux/amd64
```

> Run this from the `catalogue-service` directory so the build context and
> `target/*.jar` belong to catalogue-service — a build from the wrong folder
> once produced a "catalogue" image that actually contained identity-service
> (see "Gotchas").

## Step 3 — Register the task definition

`deployment/catalogue-taskdef.json` defines the container (image, port 8082,
`LabRole` for execution/task roles, log config, and the shared `JWT_SECRET`).

```bash
aws ecs register-task-definition \
  --cli-input-json file://deployment/catalogue-taskdef.json \
  --region "$AWS_REGION"
```

## Step 4 — Networking (subnet + security group)

```bash
VPC_ID=$(aws ec2 describe-vpcs --filters "Name=isDefault,Values=true" \
  --query "Vpcs[0].VpcId" --output text --region "$AWS_REGION")
SUBNET_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" \
  --query "Subnets[0].SubnetId" --output text --region "$AWS_REGION")

# Reuse the SG if it exists, otherwise create it and open 8082
SG_ID=$(aws ec2 describe-security-groups \
  --filters "Name=group-name,Values=catalogue-service-sg" "Name=vpc-id,Values=$VPC_ID" \
  --query "SecurityGroups[0].GroupId" --output text --region "$AWS_REGION")

if [ "$SG_ID" = "None" ] || [ -z "$SG_ID" ]; then
  SG_ID=$(aws ec2 create-security-group --group-name catalogue-service-sg \
    --description "catalogue-service Fargate" --vpc-id "$VPC_ID" \
    --region "$AWS_REGION" --query "GroupId" --output text)
  aws ec2 authorize-security-group-ingress --group-id "$SG_ID" \
    --protocol tcp --port 8082 --cidr 0.0.0.0/0 --region "$AWS_REGION"
fi

echo "SUBNET_ID=$SUBNET_ID  SG_ID=$SG_ID"
```

## Step 5 — Run the task

```bash
aws ecs run-task --cluster theatre-cluster --launch-type FARGATE \
  --task-definition catalogue-service --count 1 \
  --network-configuration "awsvpcConfiguration={subnets=[$SUBNET_ID],securityGroups=[$SG_ID],assignPublicIp=ENABLED}" \
  --region "$AWS_REGION"
```

`assignPublicIp=ENABLED` is required so the task can pull from ECR and be reachable.

## Step 6 — Get the public IP and verify

```bash
TASK_ARN=$(aws ecs list-tasks --cluster theatre-cluster --family catalogue-service \
  --query "taskArns[0]" --output text --region "$AWS_REGION")

# Confirm it reached RUNNING and started the right app
aws ecs describe-tasks --cluster theatre-cluster --tasks "$TASK_ARN" \
  --query "tasks[0].lastStatus" --output text --region "$AWS_REGION"
aws logs tail /ecs/catalogue-service --since 3m --region "$AWS_REGION" \
  | grep -i "Starting CatalogueService\|Tomcat started\|ERROR"

ENI_ID=$(aws ecs describe-tasks --cluster theatre-cluster --tasks "$TASK_ARN" \
  --query "tasks[0].attachments[0].details[?name=='networkInterfaceId'].value" \
  --output text --region "$AWS_REGION")
PUBLIC_IP=$(aws ec2 describe-network-interfaces --network-interface-ids "$ENI_ID" \
  --query "NetworkInterfaces[0].Association.PublicIp" --output text --region "$AWS_REGION")

echo "http://$PUBLIC_IP:8082"
curl -s -o /dev/null -w "%{http_code}\n" "http://$PUBLIC_IP:8082/actuator/health"   # expect 200
```

Swagger UI: `http://$PUBLIC_IP:8082/swagger-ui.html`

Note: catalogue-service endpoints require a valid JWT. Obtain one from
identity-service (`POST /auth/login`) and send it as `Authorization: Bearer <token>`.

## Redeploy after code changes

```bash
./mvnw -DskipTests clean package
docker buildx build --platform linux/amd64 -t "$IMAGE_URI" --push .
OLD_TASK=$(aws ecs list-tasks --cluster theatre-cluster --family catalogue-service \
  --query "taskArns[0]" --output text --region "$AWS_REGION")
aws ecs stop-task --cluster theatre-cluster --task "$OLD_TASK" --region "$AWS_REGION"
# then re-run Step 5
```

## Logs

```bash
aws logs tail /ecs/catalogue-service --since 15m --region us-east-1
```
