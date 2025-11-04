package com.logitrack.logitrack.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "warehouses")
@Getter // Ajoute tous les getters
@Setter // Ajoute tous les setters
@NoArgsConstructor // Ajoute le constructeur sans argument (requis par JPA)
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private boolean active = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_manager_user_id", referencedColumnName = "id")
    private User warehouseManager;

    @PrePersist
    public void generateCode() {
        if (this.code == null || this.code.isEmpty()) {
            // Generate unique code: WH-{name first 3 chars}-{timestamp last 6 digits}
            String namePrefix = this.name != null && this.name.length() >= 3 
                ? this.name.substring(0, 3).toUpperCase() 
                : "WH";
            this.code = String.format("WH-%s-%06d", namePrefix, System.currentTimeMillis() % 1000000);
        }
    }
}