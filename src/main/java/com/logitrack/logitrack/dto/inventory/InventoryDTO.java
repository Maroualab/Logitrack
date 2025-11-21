package com.logitrack.logitrack.dto.inventory;

import lombok.Data;

@Data
public class InventoryDTO {

    private Long id;

    private Long productId;
    private Long warehouseId;

    private String productSku;
    private String warehouseCode;

    private int qtyOnHand;
    private int qtyReserved;


    public int getQtyAvailable() {
        return this.qtyOnHand - this.qtyReserved;
    }
}