package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.config.AuthenticatedUser;
import com.theatre.catalogueservice.model.PerformanceRequest;
import com.theatre.catalogueservice.model.PerformanceResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Performances", description = "Read, create, update and delete individual performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(summary = "Get a performance by id",
            description = "Returns a single performance identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Performance found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceResponse> getPerformanceById(
            @Parameter(description = "Unique identifier of the performance") @PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformanceById(id));
    }

    @Operation(summary = "Create a performance",
            description = "Creates a new performance. The authenticated user is recorded as the creator.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Performance created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @PostMapping
    public ResponseEntity<PerformanceResponse> createPerformance(@RequestBody PerformanceRequest request,
                                                                 @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(performanceService.createPerformance(request, user.displayName()));
    }

    @Operation(summary = "Update a performance",
            description = "Updates an existing performance identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Performance updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PerformanceResponse> updatePerformance(
            @Parameter(description = "Unique identifier of the performance") @PathVariable UUID id,
            @RequestBody PerformanceRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(performanceService.updatePerformance(id, request, user.displayName()));
    }

    @Operation(summary = "Delete a performance",
            description = "Deletes an existing performance identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Performance deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(
            @Parameter(description = "Unique identifier of the performance") @PathVariable UUID id,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        performanceService.deletePerformance(id, user.displayName());
        return ResponseEntity.noContent().build();
    }
}
