package com.theatre.catalogueservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProductionSummaryResponse extends CommonResponse {
    private long active;
    private long upcoming;
    private long inactive;
    private long total;
}
