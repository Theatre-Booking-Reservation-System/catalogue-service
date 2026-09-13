package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.model.PerformanceListResponse;
import com.theatre.catalogueservice.model.ProductionListResponse;
import com.theatre.catalogueservice.model.ProductionRequest;
import com.theatre.catalogueservice.model.ProductionResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import com.theatre.catalogueservice.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.theatre.catalogueservice.config.AuthenticatedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<ProductionResponse> createProduction(@RequestBody ProductionRequest request,
                                                               @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productionService.createProduction(request, user.displayName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionResponse> updateProduction(@PathVariable UUID id,
                                                               @RequestBody ProductionRequest request,
                                                               @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(productionService.updateProduction(id, request, user.displayName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduction(@PathVariable UUID id,
                                                 @AuthenticationPrincipal AuthenticatedUser user) {
        productionService.deleteProduction(id, user.displayName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/performances")
    public ResponseEntity<PerformanceListResponse> getPerformancesByProductionId(@PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformancesByProductionId(id));
    }

}
