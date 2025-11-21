package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.warehouse.WarehouseDTO;
import com.logitrack.logitrack.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;


    @PostMapping("/create")
    public ResponseEntity<WarehouseDTO> createWarehouse(@Valid @RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO createdWarehouse = warehouseService.createWarehouse(warehouseDTO);
        return new ResponseEntity<>(createdWarehouse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        WarehouseDTO warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        List<WarehouseDTO> warehouses = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(warehouses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(@PathVariable Long id,
                                                        @Valid @RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO updatedWarehouse = warehouseService.updateWarehouse(id, warehouseDTO);
        return ResponseEntity.ok(updatedWarehouse);
    }

    @PostMapping("/{code}")
    public ResponseEntity<WarehouseDTO> findWarehouse(@PathVariable String code){
        WarehouseDTO warehouseDTO = warehouseService.getWarehouseByCode(code);
        return ResponseEntity.ok(warehouseDTO);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<WarehouseDTO> deactivateWarehouse(@PathVariable Long id) {
        WarehouseDTO deactivatedWarehouse = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(deactivatedWarehouse);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}