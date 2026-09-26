-- ============================================================
-- Manual schema reset for catalogue-service (PostgreSQL).
--
-- Use this ONLY when you intend to drop and recreate the production
-- table after the title/description column changes (single `title` and
-- `description` columns replacing the per-language variants, plus the
-- new duration / age_restriction / cast_crew columns).
--
-- This script is NOT run automatically. Run it by hand against
-- catalogue_db, e.g.:
--   psql -h localhost -U <user> -d catalogue_db -f reset-production-schema.sql
--
-- After running it, start catalogue-service with spring.jpa.hibernate.ddl-auto
-- = update (the default) so Hibernate recreates the tables from the JPA
-- entities, then data.sql reseeds them.
-- ============================================================

-- performance has a FK to production, so drop it first (or use CASCADE).
DROP TABLE IF EXISTS performance CASCADE;
DROP TABLE IF EXISTS production CASCADE;

-- Optional: recreate the production table explicitly to match the current
-- entity mapping. If you rely on Hibernate ddl-auto=update you can skip this
-- CREATE and let the app build the schema on startup.
CREATE TABLE production (
    production_id     UUID PRIMARY KEY,
    title             VARCHAR(255)   NOT NULL,
    language          VARCHAR(20)    NOT NULL,
    genre             VARCHAR(255),
    description       TEXT,
    base_ticket_cost  NUMERIC(10, 2) NOT NULL,
    duration          VARCHAR(255),
    age_restriction   VARCHAR(255),
    cast_crew         TEXT,
    release_date      DATE           NOT NULL,
    end_date          DATE,
    poster_image_url  TEXT,
    status            INTEGER        NOT NULL,
    added_by          VARCHAR(255),
    added_date        TIMESTAMP,
    modified_by       VARCHAR(255),
    modified_date     TIMESTAMP
);
