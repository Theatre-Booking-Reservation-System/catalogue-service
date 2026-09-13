package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.config.AuthenticatedUser;
import com.theatre.catalogueservice.model.PerformanceRequest;
import com.theatre.catalogueservice.model.PerformanceResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getPerformanceById(@PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformanceById(id));
    }

    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest request,
                                                                 @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(performanceService.createPerformance(request, user.displayName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceResponse> updatePerformance(@PathVariable UUID id,
                                                                 @RequestBody PerformanceRequest request,
                                                                 @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, request, user.displayName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(@PathVariable UUID id,
                                                  @AuthenticationPrincipal AuthenticatedUser user) {
        performanceService.deletePerformance(id, user.displayName());
        return ResponseEntity.noContent().build();
    }
}
