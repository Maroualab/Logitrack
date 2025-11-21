package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByCode(String code);
    //List<Inventory> GetWarehouseInventoryByWarehouseId(Long warehouseId);
    // Optional<Warehouse> findByWarehouseManager(User manager);
}