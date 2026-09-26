package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.SessionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PerformanceResponse extends CommonResponse {
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
