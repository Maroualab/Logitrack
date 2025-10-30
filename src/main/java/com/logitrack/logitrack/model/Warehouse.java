package com.logitrack.logitrack.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;

    private String name;
    private String address;
    private Boolean active = true;
    private LocalTime cutOffTime = LocalTime.of(15, 0); // 3:00 PM

    // Constructors
    public Warehouse() {}

    public Warehouse(Long id, String code, String name, String address, Boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.address = address;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalTime getCutOffTime() { return cutOffTime; }
    public void setCutOffTime(LocalTime cutOffTime) { this.cutOffTime = cutOffTime; }
}