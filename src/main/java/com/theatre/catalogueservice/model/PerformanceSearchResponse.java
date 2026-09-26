package com.theatre.catalogueservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PerformanceSearchResponse extends CommonResponse {
    private List<PerformanceItem> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
