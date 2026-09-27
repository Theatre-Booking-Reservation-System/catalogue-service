package com.theatre.catalogueservice.repository.spec;

import com.theatre.catalogueservice.repository.model.Production;
import com.theatre.catalogueservice.util.Language;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class ProductionSpecifications {

    private ProductionSpecifications() {
    }

    // Matches everything — used when a filter is absent (identity for AND composition)
    private static Specification<Production> always() {
        return (root, query, cb) -> cb.conjunction();
    }

    // Case-insensitive partial match on the title
    public static Specification<Production> titleContains(String title) {
        if (title == null || title.isBlank()) {
            return always();
        }
        String like = "%" + title.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), like);
    }

    // Case-insensitive partial match on genre
    public static Specification<Production> hasGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            return always();
        }
        String like = "%" + genre.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get("genre")), like);
    }

    public static Specification<Production> hasLanguage(Language language) {
        if (language == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("language"), language);
    }

    // Productions releasing on or after the given date (inclusive lower bound)
    public static Specification<Production> releaseDateFrom(LocalDate releaseDate) {
        if (releaseDate == null) {
            return always();
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("releaseDate"), releaseDate);
    }

    // Productions whose run ends on or before the given date (inclusive upper bound).
    // Open-ended runs (null endDate) are excluded when this filter is applied.
    public static Specification<Production> endDateTo(LocalDate endDate) {
        if (endDate == null) {
            return always();
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    public static Specification<Production> hasStatus(Integer status) {
        if (status == null) {
            return always();
        }
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
