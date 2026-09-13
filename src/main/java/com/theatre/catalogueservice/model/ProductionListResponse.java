package com.theatre.catalogueservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProductionListResponse extends CommonResponse {
    private List<ProductionItem> productions;
}
