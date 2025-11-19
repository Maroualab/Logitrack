package com.logitrack.logitrack.controller;


import com.logitrack.logitrack.dto.inventory.InventoryDTO;
import com.logitrack.logitrack.dto.purchaseOrder.PurchaseOrderDTO;
import com.logitrack.logitrack.dto.purchaseOrder.PurchaseReceiveDTO;
import com.logitrack.logitrack.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;


    @PostMapping("/create")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@Valid @RequestBody PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrderDTO createdPO = purchaseOrderService.createPurchaseOrder(purchaseOrderDTO);
        return new ResponseEntity<>(createdPO, HttpStatus.CREATED);
    }


    @PostMapping("/lines/{poLineId}/receive")
    public ResponseEntity<InventoryDTO> receiveItems(
            @PathVariable Long poLineId,
            @Valid @RequestBody PurchaseReceiveDTO receiveDTO) {

        InventoryDTO updatedInventory = purchaseOrderService.receiveItems(
                poLineId,
                receiveDTO.getWarehouseId(),
                receiveDTO.getQuantityToReceive()
        );

        return ResponseEntity.ok(updatedInventory);
    }

    


}