package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.Language;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductionItem {
    private UUID productionId;
    private String title;
    private Language language;
    private String genre;
    private String description;
    private BigDecimal baseTicketCost;
    private String duration;
    private String ageRestriction;
    private List<CastCrewMember> castCrew;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String posterImageUrl;
    private Integer status;
}
