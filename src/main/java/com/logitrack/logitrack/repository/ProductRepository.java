package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find product by SKU
    Optional<Product> findBySku(String sku);

    // Find all active products
    List<Product> findByActiveTrue();

    // Find products by category
    List<Product> findByCategory(String category);

    // Check if SKU already exists
    boolean existsBySku(String sku);

    // Find products by name containing (for search)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find products by name or description containing (for search)
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}