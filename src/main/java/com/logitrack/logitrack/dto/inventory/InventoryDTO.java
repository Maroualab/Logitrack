package com.logitrack.logitrack.dto.inventory;

import lombok.Data;

@Data // @Data inclut @Getter, @Setter, @ToString, etc.
public class InventoryDTO {

    private Long id;

    // On veut savoir de quel produit et entrepôt il s'agit
    private Long productId;
    private Long warehouseId;

    // C'est très utile d'inclure aussi les "noms"
    private String productSku;
    private String warehouseCode;

    private int qtyOnHand;
    private int qtyReserved;


    public int getQtyAvailable() {
        return this.qtyOnHand - this.qtyReserved;
    }
}