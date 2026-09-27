package com.theatre.catalogueservice.model;

import com.theatre.catalogueservice.util.SessionType;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class PerformanceRequest {
    private UUID productionId;
    private LocalDate date;
    private LocalTime time;
    private SessionType sessionType;
}
