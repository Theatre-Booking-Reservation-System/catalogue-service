package com.theatre.catalogueservice.controller;

import com.theatre.catalogueservice.model.PerformanceListResponse;
import com.theatre.catalogueservice.model.ProductionListResponse;
import com.theatre.catalogueservice.model.ProductionRequest;
import com.theatre.catalogueservice.model.ProductionResponse;
import com.theatre.catalogueservice.model.ProductionSearchResponse;
import com.theatre.catalogueservice.model.ProductionSummaryResponse;
import com.theatre.catalogueservice.service.PerformanceService;
import com.theatre.catalogueservice.service.ProductionService;
import com.theatre.catalogueservice.util.Language;
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
import com.theatre.catalogueservice.config.AuthenticatedUser;
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
@RequestMapping("/productions")
@RequiredArgsConstructor
@Tag(name = "Productions", description = "Create, read, update and delete theatre productions and list their performances")
public class ProductionController {

    private final ProductionService productionService;
    private final PerformanceService performanceService;

    @Operation(summary = "List all productions",
            description = "Returns every production in the catalogue.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productions returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping
    public ResponseEntity<ProductionListResponse> getAllProductions() {
        return ResponseEntity.ok(productionService.getAllProductions());
    }

    @Operation(summary = "Search productions (paginated)",
            description = "Paginated, filterable search for the Productions page. "
                    + "Filter by title, status (1=Active, 9=Inactive), genre, "
                    + "language (SINHALA/TAMIL/ENGLISH), releaseDate (on or after) and "
                    + "endDate (on or before). Supports page, size and sort query params, "
                    + "e.g. ?page=0&size=10&sort=releaseDate,desc.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Matching productions returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping("/search")
    public ResponseEntity<ProductionSearchResponse> searchProductions(
            @Parameter(description = "Title filter (case-insensitive partial match)")
            @RequestParam(required = false) String title,
            @Parameter(description = "Exact status filter: 1=Active, 9=Inactive/Archived")
            @RequestParam(required = false) Integer status,
            @Parameter(description = "Genre filter (case-insensitive partial match)")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Language filter: SINHALA, TAMIL or ENGLISH")
            @RequestParam(required = false) Language language,
            @Parameter(description = "Return productions releasing on or after this date (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
            @Parameter(description = "Return productions whose run ends on or before this date (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(hidden = true) @PageableDefault(size = 10, sort = "releaseDate") Pageable pageable) {
        return ResponseEntity.ok(productionService.search(
                title, status, genre, language, releaseDate, endDate, pageable));
    }

    @Operation(summary = "Production summary counts",
            description = "Aggregate counts for the dashboard 'Active Productions' widget: "
                    + "active (running), upcoming (releasing later), inactive (archived) and total.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Summary counts returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @GetMapping("/summary")
    public ResponseEntity<ProductionSummaryResponse> productionSummary() {
        return ResponseEntity.ok(productionService.summary());
    }

    @Operation(summary = "Get a production by id",
            description = "Returns a single production identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Production found"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No production exists for the given id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductionResponse> getProductionById(
            @Parameter(description = "Unique identifier of the production") @PathVariable UUID id) {
        return ResponseEntity.ok(productionService.getProductionById(id));
    }

    @Operation(summary = "Create a production",
            description = "Creates a new production. The authenticated user is recorded as the creator.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Production created"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT")
    })
    @PostMapping
    public ResponseEntity<ProductionResponse> createProduction(@RequestBody ProductionRequest request,
                                                               @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productionService.createProduction(request, user.displayName()));
    }

    @Operation(summary = "Update a production",
            description = "Updates an existing production identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Production updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No production exists for the given id")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductionResponse> updateProduction(
            @Parameter(description = "Unique identifier of the production") @PathVariable UUID id,
            @RequestBody ProductionRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        return ResponseEntity.ok(productionService.updateProduction(id, request, user.displayName()));
    }

    @Operation(summary = "Delete a production",
            description = "Deletes an existing production identified by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Production deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No production exists for the given id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduction(
            @Parameter(description = "Unique identifier of the production") @PathVariable UUID id,
            @Parameter(hidden = true) @AuthenticationPrincipal AuthenticatedUser user) {
        productionService.deleteProduction(id, user.displayName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List performances for a production",
            description = "Returns every performance scheduled for the given production.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Performances returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No production exists for the given id")
    })
    @GetMapping("/{id}/performances")
    public ResponseEntity<PerformanceListResponse> getPerformancesByProductionId(
            @Parameter(description = "Unique identifier of the production") @PathVariable UUID id) {
        return ResponseEntity.ok(performanceService.getPerformancesByProductionId(id));
    }

}
