package com.theatre.catalogueservice.repository.spec;

import com.theatre.catalogueservice.repository.model.Production;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ProductionSpecifications {

    private ProductionSpecifications() {
    }

    // Matches everything — used when a filter is absent (identity for AND composition)
    private static Specification<Production> always() {
        return (root, query, cb) -> cb.conjunction();
    }

    // Case-insensitive match of the query text against any title (en/si/ta) or genre
    public static Specification<Production> textContains(String q) {
        if (q == null || q.isBlank()) {
            return always();
        }
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("titleEn")), like),
                cb.like(cb.lower(root.get("titleSi")), like),
                cb.like(cb.lower(root.get("titleTa")), like),
                cb.like(cb.lower(root.get("genre")), like)
        );
    }

    public static Specification<Production> hasStatus(Integer status) {
        if (status == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Production> upcoming(Boolean upcoming, LocalDate today) {
        if (upcoming == null) {
            return always();
        }
        return (root, query, cb) -> upcoming
                ? cb.greaterThan(root.get("releaseDate"), today)
                : cb.lessThanOrEqualTo(root.get("releaseDate"), today);
    }
}
