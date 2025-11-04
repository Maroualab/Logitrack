package com.logitrack.logitrack.model;

import com.logitrack.logitrack.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String fullName;
    private Boolean active = true;

    @OneToOne(mappedBy = "warehouseManager", fetch = FetchType.LAZY)
    private Warehouse managedWarehouse;
    }

