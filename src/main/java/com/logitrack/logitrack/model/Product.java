package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String sku;

    private String name;
    private String category;
    private Boolean active = true;


    @PrePersist
    public void generateSku() {
        if (this.sku == null) {
            String categoryPrefix = this.category != null ?
                    this.category.substring(0, Math.min(3, this.category.length())).toUpperCase() : "GEN";
            this.sku = String.format("PROD-%s-%06d", categoryPrefix,
                    this.id != null ? this.id : System.currentTimeMillis() % 1000000);
        }
    }


 }