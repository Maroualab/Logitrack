package com.logitrack.logitrack.dto.warehouse;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data // Parfait pour les DTOs
@NoArgsConstructor // Utile pour la désérialisation JSON
public class WarehouseDTO {

    private Long id;

    @Size(min = 3, max = 20, message = "Code must be between 3 and 20 characters")
    private String code; 

    @NotBlank(message = "Warehouse name cannot be blank")
    private String name;

    private boolean active;

    @NotNull(message = "Warehouse Manager ID cannot be null")
    private Long warehouseManagerId;
}