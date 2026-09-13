package com.theatre.catalogueservice.repository;

import com.theatre.catalogueservice.repository.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductionRepository extends JpaRepository<Production, UUID> {
    List<Production> findByStatus(Integer status);
}
