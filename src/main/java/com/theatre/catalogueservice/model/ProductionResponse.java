package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.Language;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ProductionResponse extends CommonResponse {
    private UUID productionId;
    private String title;
    private Language language;
    private String genre;
    private String description;
    private BigDecimal baseTicketCost;
    private String duration;
    private Integer ageRestriction;
    private String castCrew;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String posterImageUrl;
    private Integer status;
}
