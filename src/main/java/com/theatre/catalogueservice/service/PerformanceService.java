package com.theatre.catalogueservice.service;

import com.theatre.catalogueservice.exception.ServiceException;
import com.theatre.catalogueservice.model.PerformanceItem;
import com.theatre.catalogueservice.model.PerformanceListResponse;
import com.theatre.catalogueservice.repository.PerformanceRepository;
import com.theatre.catalogueservice.repository.ProductionRepository;
import com.theatre.catalogueservice.repository.model.Performance;
import com.theatre.catalogueservice.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final ProductionRepository productionRepository;

    public PerformanceListResponse getPerformancesByProductionId(UUID productionId) {
        if (!productionRepository.existsById(productionId)) {
            throw new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND);
        }

        List<PerformanceItem> performances = performanceRepository.findByProductionId(productionId)
                .stream()
                .map(this::toPerformanceModel)
                .toList();

        return PerformanceListResponse.builder()
                .performances(performances)
                .build();
    }

    private PerformanceItem toPerformanceModel(Performance p) {
        return PerformanceItem.builder()
                .performanceId(p.getPerformanceId())
                .productionId(p.getProductionId())
                .date(p.getDate())
                .time(p.getTime())
                .sessionType(p.getSessionType())
                .releaseDate(p.getReleaseDate())
                .earlyAccessOpensAt(p.getEarlyAccessOpensAt())
                .isEarlyAccessActive(p.getIsEarlyAccessActive())
                .status(p.getStatus())
                .build();
    }
}
