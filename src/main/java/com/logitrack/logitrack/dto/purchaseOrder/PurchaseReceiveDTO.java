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
}