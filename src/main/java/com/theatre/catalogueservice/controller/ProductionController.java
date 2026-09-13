package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.model.PerformanceListResponse;
import com.theatre.catalogueservice.model.ProductionListResponse;
import com.theatre.catalogueservice.model.ProductionResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import com.theatre.catalogueservice.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/productions")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionService productionService;
    private final PerformanceService performanceService;

    @GetMapping
    public ResponseEntity<ProductionListResponse> getAllProductions() {
        return ResponseEntity.ok(productionService.getAllProductions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionResponse> getProductionById(@PathVariable UUID id) {
        return ResponseEntity.ok(productionService.getProductionById(id));
    }

    @GetMapping("/{id}/performances")
    public ResponseEntity<PerformanceListResponse> getPerformancesByProductionId(@PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformancesByProductionId(id));
    }

}
