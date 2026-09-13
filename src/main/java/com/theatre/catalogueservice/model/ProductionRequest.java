package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.Language;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductionRequest {
    private String titleEn;
    private String titleSi;
    private String titleTa;
    private Language language;
    private String genre;
    private String descriptionEn;
    private String descriptionSi;
    private String descriptionTa;
    private BigDecimal baseTicketCost;
    private LocalDate releaseDate;
    private Integer status;
}
