package com.logitrack.logitrack.dto.warehouse;

import com.logitrack.logitrack.dto.inventory.InventoryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data // Parfait pour les DTOs
@NoArgsConstructor // Utile pour la désérialisation JSON
public class WarehouseDTO {

    private Long id;

    private String code;

    @NotBlank(message = "Warehouse name cannot be blank")
    private String name;

    private boolean active;

    @NotNull(message = "Warehouse Manager ID cannot be null")
    private Long warehouseManagerId;
    
    private List<InventoryDTO> inventory;
}