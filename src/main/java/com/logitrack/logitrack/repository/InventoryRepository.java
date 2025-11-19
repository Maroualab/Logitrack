package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {



    Optional<Inventory> findByProduct_Sku( String sku);

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);
}