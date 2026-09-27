package com.theatre.catalogueservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProductionCreateResponse extends CommonResponse {
    private UUID productionId;
}
