package com.logitrack.logitrack.dto.purchaseOrder;


import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PurchaseReceiveDTO {

    @NotNull(message = "Warehouse ID (where items are received) cannot be null")
    private Long warehouseId;

    @NotNull(message = "Quantity to receive cannot be null")
    @Min(value = 1, message = "Quantity to receive must be at least 1")
    private int quantityToReceive;
}