package com.theatre.catalogueservice.service;

import com.theatre.catalogueservice.exception.ServiceException;
import com.theatre.catalogueservice.model.PerformanceItem;
import com.theatre.catalogueservice.model.PerformanceListResponse;
import com.theatre.catalogueservice.model.PerformanceRequest;
import com.theatre.catalogueservice.model.PerformanceResponse;
import com.theatre.catalogueservice.model.PerformanceSearchResponse;
import com.theatre.catalogueservice.repository.PerformanceRepository;
import com.theatre.catalogueservice.repository.ProductionRepository;
import com.theatre.catalogueservice.repository.model.Performance;
import com.theatre.catalogueservice.repository.spec.PerformanceSpecifications;
import com.theatre.catalogueservice.util.ErrorCode;
import com.theatre.catalogueservice.util.ProductionStatus;
import com.theatre.catalogueservice.util.SessionType;
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

    public PerformanceSearchResponse search(UUID productionId, LocalDate dateFrom, LocalDate dateTo,
                                            SessionType sessionType, Integer status, Pageable pageable) {
        Specification<Performance> spec = Specification.allOf(
                PerformanceSpecifications.forProduction(productionId),
                PerformanceSpecifications.dateFrom(dateFrom),
                PerformanceSpecifications.dateTo(dateTo),
                PerformanceSpecifications.hasSessionType(sessionType),
                PerformanceSpecifications.hasStatus(status)
        );

        Page<PerformanceItem> result = performanceRepository.findAll(spec, pageable)
                .map(this::toPerformanceModel);

        return PerformanceSearchResponse.builder()
                .content(result.getContent())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }

    public PerformanceResponse getPerformanceById(UUID id) {
        return performanceRepository.findById(id)
                .map(this::toPerformanceResponse)
                .orElseThrow(() -> new ServiceException(ErrorCode.PERFORMANCE_NOT_FOUND));
    }

    @Transactional
    public PerformanceResponse createPerformance(PerformanceRequest request, String email) {
        if (!productionRepository.existsById(request.getProductionId())) {
            throw new ServiceException(ErrorCode.PRODUCTION_NOT_FOUND);
        }

        Performance performance = new Performance();
        applyRequest(performance, request);
        performance.setStatus(request.getStatus() != null
                ? request.getStatus()
                : ProductionStatus.ACTIVE.getValue());
        performance.setAddedBy(email);
        performance.setAddedDate(LocalDateTime.now());

        return toPerformanceResponse(performanceRepository.save(performance));
    }

    @Transactional
    public PerformanceResponse updatePerformance(UUID id, PerformanceRequest request, String email) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.PERFORMANCE_NOT_FOUND));

        applyRequest(performance, request);
        if (request.getStatus() != null) {
            performance.setStatus(request.getStatus());
        }
        performance.setModifiedBy(email);
        performance.setModifiedDate(LocalDateTime.now());

        return toPerformanceResponse(performanceRepository.save(performance));
    }

    @Transactional
    public void deletePerformance(UUID id, String email) {
        Performance performance = performanceRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ErrorCode.PERFORMANCE_NOT_FOUND));

        performance.setStatus(ProductionStatus.ARCHIVED.getValue());
        performance.setModifiedBy(email);
        performance.setModifiedDate(LocalDateTime.now());

        performanceRepository.save(performance);
    }

    private void applyRequest(Performance performance, PerformanceRequest request) {
        performance.setProductionId(request.getProductionId());
        performance.setDate(request.getDate());
        performance.setTime(request.getTime());
        performance.setSessionType(request.getSessionType());
    }

    private PerformanceResponse toPerformanceResponse(Performance p) {
        return PerformanceResponse.builder()
                .performanceId(p.getPerformanceId())
                .productionId(p.getProductionId())
                .date(p.getDate())
                .time(p.getTime())
                .sessionType(p.getSessionType())
                .status(p.getStatus())
                .build();
    }

    private PerformanceItem toPerformanceModel(Performance p) {
        return PerformanceItem.builder()
                .performanceId(p.getPerformanceId())
                .productionId(p.getProductionId())
                .date(p.getDate())
                .time(p.getTime())
                .sessionType(p.getSessionType())
                .status(p.getStatus())
                .build();
    }
}
