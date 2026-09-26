package com.theatre.catalogueservice.repository.spec;

import com.theatre.catalogueservice.repository.model.Performance;
import com.theatre.catalogueservice.util.SessionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public final class PerformanceSpecifications {

    private PerformanceSpecifications() {
    }

    // Matches everything — used when a filter is absent (identity for AND composition)
    private static Specification<Performance> always() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Performance> forProduction(UUID productionId) {
        if (productionId == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("productionId"), productionId);
    }

    public static Specification<Performance> dateFrom(LocalDate from) {
        if (from == null) {
            return always();
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<Performance> dateTo(LocalDate to) {
        if (to == null) {
            return always();
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<Performance> hasSessionType(SessionType sessionType) {
        if (sessionType == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("sessionType"), sessionType);
    }

    public static Specification<Performance> hasStatus(Integer status) {
        if (status == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
