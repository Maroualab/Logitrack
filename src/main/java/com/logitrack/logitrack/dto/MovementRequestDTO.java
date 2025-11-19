package com.logitrack.logitrack.dto;

import com.logitrack.logitrack.model.enums.MovementType;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Data
public class MovementRequestDTO {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Warehouse ID cannot be null")
    private Long warehouseId;


    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Movement type cannot be null")
    private MovementType type;

    // Optionnel, pour la traçabilité (ex: "PO-123" ou "SO-500")
     private String referenceDocument;
}