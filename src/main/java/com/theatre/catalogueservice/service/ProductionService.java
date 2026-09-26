package com.theatre.catalogueservice.service;

import com.theatre.catalogueservice.exception.ServiceException;
import com.theatre.catalogueservice.model.ProductionItem;
import com.theatre.catalogueservice.model.ProductionListResponse;
import com.theatre.catalogueservice.model.ProductionRequest;
import com.theatre.catalogueservice.model.ProductionResponse;
import com.theatre.catalogueservice.model.ProductionSearchResponse;
import com.theatre.catalogueservice.model.ProductionSummaryResponse;
import com.theatre.catalogueservice.repository.ProductionRepository;
import com.theatre.catalogueservice.repository.model.Production;
import com.theatre.catalogueservice.repository.spec.ProductionSpecifications;
import com.theatre.catalogueservice.util.ErrorCode;
import com.theatre.catalogueservice.util.ProductionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductionService {

    private final ProductionRepository productionRepository;

    public ProductionListResponse getAllProductions() {
        List<ProductionItem> productions = productionRepository.findByStatus(ProductionStatus.ACTIVE.getValue())
                .stream()
                .map(this::toProductionModel)
                .toList();

        return ProductionListResponse.builder()
                .productions(productions)
                .build();
    }

    public ProductionSearchResponse search(String q, Integer status, Boolean upcoming, Pageable pageable) {
        Specification<Production> spec = Specification.allOf(
                ProductionSpecifications.textContains(q),
                ProductionSpecifications.hasStatus(status),
                ProductionSpecifications.upcoming(upcoming, LocalDate.now())
        );

        Page<ProductionItem> result = productionRepository.findAll(spec, pageable)
                .map(this::toProductionModel);

        return ProductionSearchResponse.builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public ProductionSummaryResponse summary() {
        LocalDate today = LocalDate.now();
        int activeStatus = ProductionStatus.ACTIVE.getValue();
        int archivedStatus = ProductionStatus.ARCHIVED.getValue();

        long activeTotal = productionRepository.countByStatus(activeStatus);
        long upcoming = productionRepository.countByStatusAndReleaseDateAfter(activeStatus, today);
        long inactive = productionRepository.countByStatus(archivedStatus);

        return ProductionSummaryResponse.builder()
                .active(activeTotal - upcoming)
                .upcoming(upcoming)
                .inactive(inactive)
                .total(productionRepository.count())
                .build();
    }

    public ProductionResponse getProductionById(UUID id) {
        return productionRepository.findById(id)
                .map(this::toProductionResponse)
                .orElseThrow(() -> new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND));
    }

    @Transactional
    public ProductionResponse createProduction(ProductionRequest request, String email) {
        Production production = new Production();
        applyRequest(production, request);
        production.setStatus(request.getStatus() != null
                ? request.getStatus()
                : ProductionStatus.ACTIVE.getValue());
        production.setAddedBy(email);
        production.setAddedDate(LocalDateTime.now());

        return toProductionResponse(productionRepository.save(production));
    }

    @Transactional
    public ProductionResponse updateProduction(UUID id, ProductionRequest request, String email) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND));

        applyRequest(production, request);
        if (request.getStatus() != null) {
            production.setStatus(request.getStatus());
        }
        production.setModifiedBy(email);
        production.setModifiedDate(LocalDateTime.now());

        return toProductionResponse(productionRepository.save(production));
    }

    @Transactional
    public void deleteProduction(UUID id, String email) {
        Production production = productionRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND));

        production.setStatus(ProductionStatus.ARCHIVED.getValue());
        production.setModifiedBy(email);
        production.setModifiedDate(LocalDateTime.now());

        productionRepository.save(production);
    }

    private void applyRequest(Production production, ProductionRequest request) {
        production.setTitleEn(request.getTitleEn());
        production.setTitleSi(request.getTitleSi());
        production.setTitleTa(request.getTitleTa());
        production.setLanguage(request.getLanguage());
        production.setGenre(request.getGenre());
        production.setDescriptionEn(request.getDescriptionEn());
        production.setDescriptionSi(request.getDescriptionSi());
        production.setDescriptionTa(request.getDescriptionTa());
        production.setBaseTicketCost(request.getBaseTicketCost());
        production.setReleaseDate(request.getReleaseDate());
        production.setEndDate(request.getEndDate());
        production.setPosterImageUrl(request.getPosterImageUrl());
    }

    private ProductionResponse toProductionResponse(Production p) {
        return ProductionResponse.builder()
                .productionId(p.getProductionId())
                .titleEn(p.getTitleEn())
                .titleSi(p.getTitleSi())
                .titleTa(p.getTitleTa())
                .language(p.getLanguage())
                .genre(p.getGenre())
                .descriptionEn(p.getDescriptionEn())
                .descriptionSi(p.getDescriptionSi())
                .descriptionTa(p.getDescriptionTa())
                .baseTicketCost(p.getBaseTicketCost())
                .releaseDate(p.getReleaseDate())
                .endDate(p.getEndDate())
                .posterImageUrl(p.getPosterImageUrl())
                .status(p.getStatus())
                .build();
    }

    private ProductionItem toProductionModel(Production p) {
        return ProductionItem.builder()
                .productionId(p.getProductionId())
                .titleEn(p.getTitleEn())
                .titleSi(p.getTitleSi())
                .titleTa(p.getTitleTa())
                .language(p.getLanguage())
                .genre(p.getGenre())
                .descriptionEn(p.getDescriptionEn())
                .descriptionSi(p.getDescriptionSi())
                .descriptionTa(p.getDescriptionTa())
                .baseTicketCost(p.getBaseTicketCost())
                .releaseDate(p.getReleaseDate())
                .endDate(p.getEndDate())
                .posterImageUrl(p.getPosterImageUrl())
                .status(p.getStatus())
                .build();
    }
}
