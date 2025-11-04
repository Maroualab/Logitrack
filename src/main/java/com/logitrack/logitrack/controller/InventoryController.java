package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.inventory.InventoryDTO;
import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.mapper.InventoryMapper;
import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;


    @PostMapping("/movements")
    public ResponseEntity<InventoryDTO> executeMovement(@Valid @RequestBody MovementRequestDTO movementRequest) {

        Inventory updatedInventory = inventoryService.handleStockMovement(movementRequest);

        return ResponseEntity.ok(inventoryMapper.toDto(updatedInventory));
    }

}