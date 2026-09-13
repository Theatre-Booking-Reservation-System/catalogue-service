package com.theatre.catalogueservice.service;

import com.theatre.catalogueservice.exception.ServiceException;
import com.theatre.catalogueservice.model.ProductionItem;
import com.theatre.catalogueservice.model.ProductionListResponse;
import com.theatre.catalogueservice.model.ProductionResponse;
import com.theatre.catalogueservice.repository.ProductionRepository;
import com.theatre.catalogueservice.repository.model.Production;
import com.theatre.catalogueservice.util.ErrorCode;
import com.theatre.catalogueservice.util.ProductionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProductionResponse getProductionById(UUID id) {
        return productionRepository.findById(id)
                .map(this::toProductionResponse)
                .orElseThrow(() -> new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND));
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
                .status(p.getStatus())
                .build();
    }
}
