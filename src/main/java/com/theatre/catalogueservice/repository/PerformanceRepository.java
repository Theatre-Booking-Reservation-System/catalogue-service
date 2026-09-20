package com.theatre.catalogueservice.repository;

import com.theatre.catalogueservice.repository.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, UUID>, JpaSpecificationExecutor<Performance> {

    List<Performance> findByProductionId(UUID productionId);
}
