package com.theatre.catalogueservice.repository.model;

import com.theatre.catalogueservice.util.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "production")
@Getter
@Setter
@NoArgsConstructor
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "production_id", updatable = false, nullable = false)
    private UUID productionId;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "genre")
    private String genre;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "base_ticket_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseTicketCost;

    // Running length of the production, e.g. "2h 30m". Free-text to allow flexible formatting.
    @Column(name = "duration")
    private String duration;

    // Age restriction label, e.g. "All Ages", "12+", "18+" (null = no restriction).
    @Column(name = "age_restriction")
    private String ageRestriction;

    // Cast and crew details (names, roles). Stored as free-text.
    @Column(name = "cast_crew", columnDefinition = "TEXT")
    private String castCrew;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    // End of the run. Nullable: open-ended runs may not have a set end date.
    @Column(name = "end_date")
    private LocalDate endDate;

    // Poster image stored inline as a base64-encoded string (may be a data URI).
    // TEXT because base64 payloads far exceed the default varchar length.
    @Column(name = "poster_image_url", columnDefinition = "TEXT")
    private String posterImageUrl;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "added_by")
    private String addedBy;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
