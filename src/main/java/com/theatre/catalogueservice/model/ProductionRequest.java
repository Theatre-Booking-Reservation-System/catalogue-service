package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.Language;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionRequest {
    private String title;
    private Language language;
    private String genre;
    private String description;
    private BigDecimal baseTicketCost;
    private String duration;
    private String ageRestriction;
    private String castCrew;
    private LocalDate releaseDate;
    private LocalDate endDate;
    private String posterImageUrl;
    private Integer status;
}
