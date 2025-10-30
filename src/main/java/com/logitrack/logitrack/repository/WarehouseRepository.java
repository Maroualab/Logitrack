package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    // Find warehouse by code
    Optional<Warehouse> findByCode(String code);

    // Find all active warehouses
    List<Warehouse> findByActiveTrue();

    // Check if warehouse code already exists
    boolean existsByCode(String code);

    // Find warehouses by name containing (for search)
    List<Warehouse> findByNameContainingIgnoreCase(String name);
}