package com.theatre.catalogueservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PerformanceListResponse extends CommonResponse {
    private List<PerformanceItem> performances;
}
