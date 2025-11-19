package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.WarehouseMapper;
import com.logitrack.logitrack.dto.warehouse.WarehouseDTO;
import com.logitrack.logitrack.model.User;
import com.logitrack.logitrack.model.Warehouse;
import com.logitrack.logitrack.model.enums.UserRole;
import com.logitrack.logitrack.repository.UserRepository;
import com.logitrack.logitrack.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseService warehouseService;

    private User warehouseManager;
    private WarehouseDTO warehouseDTO;

    @BeforeEach
    void setUp() {
        warehouseManager = new User();
        warehouseManager.setId(1L);
        warehouseManager.setEmail("manager@test.com");
        warehouseManager.setRole(UserRole.WAREHOUSE_MANAGER);

        warehouseDTO = new WarehouseDTO();
        warehouseDTO.setCode("PARIS");
        warehouseDTO.setName("Entrepôt Paris");
        warehouseDTO.setWarehouseManagerId(1L);
    }

    @Test
    void testCreateWarehouse_Success() {
        // Arrange
        when(warehouseRepository.findByCode("PARIS")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(warehouseManager));

        // Simuler les mappers (ignorer pour ce test)
        when(warehouseMapper.toEntity(any(WarehouseDTO.class))).thenReturn(new Warehouse());
        when(warehouseMapper.toDto(any(Warehouse.class))).thenReturn(warehouseDTO);

        // Act
        WarehouseDTO result = warehouseService.createWarehouse(warehouseDTO);

        // Assert
        assertNotNull(result);
        assertEquals("PARIS", result.getCode());
        verify(warehouseRepository, times(1)).save(any(Warehouse.class));
    }

    @Test
    void testCreateWarehouse_Fails_UserNotAManager() {
        // Arrange
        User clientUser = new User();
        clientUser.setId(2L);
        clientUser.setRole(UserRole.CLIENT);

        warehouseDTO.setWarehouseManagerId(2L);
        when(warehouseRepository.findByCode("PARIS")).thenReturn(Optional.empty());
        when(userRepository.findById(2L)).thenReturn(Optional.of(clientUser));

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            warehouseService.createWarehouse(warehouseDTO);
        });

        assertTrue(ex.getMessage().contains("does not have the WAREHOUSE_MANAGER role"));
    }
}