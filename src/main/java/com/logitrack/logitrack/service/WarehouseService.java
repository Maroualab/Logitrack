package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.warehouse.WarehouseDTO;
import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.WarehouseMapper;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.enums.UserRole;
import com.logitrack.logitrack.model.Warehouse;
import com.logitrack.logitrack.repository.UserRepository;
import com.logitrack.logitrack.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserRepository userRepository;
    private final WarehouseMapper warehouseMapper;


    @Transactional
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        warehouseRepository.findByCode(warehouseDTO.getCode()).ifPresent(w -> {
            throw new BusinessException("Warehouse code '" + warehouseDTO.getCode() + "' already exists");
        });

        User manager = validateWarehouseManager(warehouseDTO.getWarehouseManagerId());

        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse.setWarehouseManager(manager);
        warehouse.setActive(true);

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(savedWarehouse);
    }

    @Transactional
    public WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        Warehouse existingWarehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        User manager = validateWarehouseManager(warehouseDTO.getWarehouseManagerId());

        existingWarehouse.setCode(warehouseDTO.getCode());
        existingWarehouse.setName(warehouseDTO.getName());
        existingWarehouse.setActive(warehouseDTO.isActive());
        existingWarehouse.setWarehouseManager(manager);

        Warehouse updatedWarehouse = warehouseRepository.save(existingWarehouse);
        return warehouseMapper.toDto(updatedWarehouse);
    }

    @Transactional
    public WarehouseDTO deactivateWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        warehouse.setActive(false);
        Warehouse deactivatedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(deactivatedWarehouse);
    }

    @Transactional(readOnly = true)
    public WarehouseDTO getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        return warehouseMapper.toDto(warehouse);
    }

    @Transactional(readOnly = true)
    public List<WarehouseDTO> getAllWarehouses() {
        return warehouseRepository.findAll()
                .stream()
                .map(warehouseMapper::toDto)
                .collect(Collectors.toList());
    }

    private User validateWarehouseManager(Long managerId) {
        if (managerId == null) {
            throw new BusinessException("Warehouse Manager ID cannot be null");
        }

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("User (Manager) not found with id: " + managerId));

        if (manager.getRole() != UserRole.WAREHOUSE_MANAGER) {
            throw new BusinessException("User " + manager.getEmail() + " does not have the WAREHOUSE_MANAGER role");
        }

        return manager;
    }
}