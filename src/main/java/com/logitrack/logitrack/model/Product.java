package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false)
    private String sku;

    private String name;
    private String description;
    private String category;
    private BigDecimal ogUnitPrice;
    private Boolean active = true;

    // Constructors
    public Product() {}

    public Product(Long id, String name, String description, String category, BigDecimal ogUnitPrice, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.ogUnitPrice = ogUnitPrice;
        this.active = active;
        generateSku();
    }

    @PrePersist
    public void generateSku() {
        if (this.sku == null) {
            String categoryPrefix = this.category != null ?
                    this.category.substring(0, Math.min(3, this.category.length())).toUpperCase() : "GEN";
            this.sku = String.format("PROD-%s-%06d", categoryPrefix,
                    this.id != null ? this.id : System.currentTimeMillis() % 1000000);
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getOgUnitPrice() { return ogUnitPrice; }
    public void setOgUnitPrice(BigDecimal unitPrice) { this.ogUnitPrice = unitPrice; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}