package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.SessionType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class PerformanceItem {
    private UUID performanceId;
    private UUID productionId;
    private LocalDate date;
    private LocalTime time;
    private SessionType sessionType;
    private LocalDate releaseDate;
    private LocalDate earlyAccessOpensAt;
    private Boolean isEarlyAccessActive;
    private Integer status;
}
