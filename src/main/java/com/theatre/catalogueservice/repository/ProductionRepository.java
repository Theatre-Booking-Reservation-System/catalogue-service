package com.theatre.catalogueservice.repository;

import com.theatre.catalogueservice.repository.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ProductionRepository extends JpaRepository<Production, UUID>, JpaSpecificationExecutor<Production> {

    List<Production> findByStatus(Integer status);

    long countByStatus(Integer status);

    // Active but not yet released == "Upcoming"
    long countByStatusAndReleaseDateAfter(Integer status, LocalDate date);
}
