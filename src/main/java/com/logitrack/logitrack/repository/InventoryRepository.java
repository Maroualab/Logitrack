package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * C'est LA méthode cruciale.
     * Elle permet de trouver l'enregistrement de stock
     * pour un produit dans un entrepôt spécifique.
     */
    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    // Une surcharge utile si on a déjà les objets entiers
    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);
}