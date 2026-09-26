package com.theatre.catalogueservice.repository.model;

import com.theatre.catalogueservice.util.SessionType;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "performance")
@Getter
@Setter
@NoArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "performance_id", updatable = false, nullable = false)
    private UUID performanceId;

    @Column(name = "production_id", nullable = false)
    private UUID productionId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false, length = 10)
    private SessionType sessionType;

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
