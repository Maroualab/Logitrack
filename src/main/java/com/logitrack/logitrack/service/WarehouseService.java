package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.Warehouse;
import com.logitrack.logitrack.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public String checkRepository() {
        try {
            long warehouseCount = warehouseRepository.count();
            return "Warehouse repository is working! Total warehouses: " + warehouseCount;
        } catch (Exception e) {
            return "Warehouse repository error: " + e.getMessage();
        }
    }
}