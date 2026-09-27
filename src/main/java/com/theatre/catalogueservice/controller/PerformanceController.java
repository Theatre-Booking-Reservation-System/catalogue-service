package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.config.AuthenticatedUser;
import com.theatre.catalogueservice.model.PerformanceCreateResponse;
import com.theatre.catalogueservice.model.PerformanceRequest;
import com.theatre.catalogueservice.model.PerformanceResponse;
import com.theatre.catalogueservice.model.PerformanceSearchResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import com.theatre.catalogueservice.util.SessionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/performances")
@RequiredArgsConstructor
@Tag(name = "Performances", description = "Read, create, update and delete individual performances")
public class PerformanceController {

    private final PerformanceService performanceService;

    @Operation(summary = "Search performances (paginated)",
            description = "Paginated, filterable search for the Performances page. Filter by "
                    + "productionId, date range (dateFrom/dateTo), sessionType and status. "
                    + "For the dashboard 'Today's Shows' widget, pass dateFrom=<today>&dateTo=<today>. "
                    + "Supports page, size and sort query params, e.g. "
                    + "?dateFrom=2026-10-15&dateTo=2026-10-15&sort=time.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matching performances returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping("/search")
    public ResponseEntity<PerformanceSearchResponse> searchPerformances(
            @Parameter(description = "Filter to a single production")
            @RequestParam(required = false) UUID productionId,
            @Parameter(description = "Inclusive lower bound on date (use today for 'Today's Shows')")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @Parameter(description = "Inclusive upper bound on date (use today for 'Today's Shows')")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @Parameter(description = "Session type filter: MATINEE or EVENING")
            @RequestParam(required = false) SessionType sessionType,
            @Parameter(description = "Exact status filter: 1=Active, 9=Inactive/Archived")
            @RequestParam(required = false) Integer status,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(
                performanceService.search(productionId, dateFrom, dateTo, sessionType, status, pageable));
    }

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
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Caller is not an ADMIN")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PerformanceCreateResponse> createPerformance(@RequestBody PerformanceRequest request,
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
            @ApiResponse(responseCode = "403", description = "Caller is not an ADMIN"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @PreAuthorize("hasRole('ADMIN')")
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
            @ApiResponse(responseCode = "403", description = "Caller is not an ADMIN"),
            @ApiResponse(responseCode = "404", description = "No performance exists for the given id")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(
            @Parameter(description = "Unique identifier of the performance") @PathVariable UUID id,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        performanceService.deletePerformance(id, user.displayName());
        return ResponseEntity.noContent().build();
    }
}
