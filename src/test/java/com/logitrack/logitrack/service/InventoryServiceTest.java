package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.StockUnavailableException;
import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.model.InventoryMovement;
import com.logitrack.logitrack.model.Warehouse;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.repository.InventoryMovementRepository;
import com.logitrack.logitrack.repository.InventoryRepository;
import com.logitrack.logitrack.repository.ProductRepository;
import com.logitrack.logitrack.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryMovementRepository movementRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Product product;
    private Warehouse warehouse;
    private MovementRequestDTO request;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setSku("TSHIRT");

        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCode("PARIS");

        request = new MovementRequestDTO();
        request.setProductId(1L);
        request.setWarehouseId(1L);
        request.setQuantity(10);
        request.setType(MovementType.INBOUND);
        request.setReferenceDocument("PO-123");

        inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setQtyOnHand(100);
        inventory.setQtyReserved(20); // 80 disponibles

        // Simuler la validation (trouver produit et entrepôt)
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
    }

    @Test
    void testHandleMovement_INBOUND_CreatesNewInventory() {
        // Arrange
        when(inventoryRepository.findByProductAndWarehouse(product, warehouse)).thenReturn(Optional.empty());

        // Act
        inventoryService.handleStockMovement(request);

        // Assert
        // Vérifier qu'on a sauvegardé un NOUVEL inventaire
        verify(inventoryRepository, times(1)).save(argThat(inv ->
                inv.getProduct().equals(product) &&
                        inv.getQtyOnHand() == 10 // 0 (initial) + 10 (requête)
        ));
        // Vérifier que le mouvement a été loggué
        verify(movementRepository, times(1)).save(any(InventoryMovement.class));
    }

    @Test
    void testHandleMovement_INBOUND_UpdatesExistingInventory() {
        // Arrange
        request.setQuantity(50);
        // L'inventaire (100) existe déjà
        when(inventoryRepository.findByProductAndWarehouse(product, warehouse)).thenReturn(Optional.of(inventory));

        // Act
        inventoryService.handleStockMovement(request);

        // Assert
        // Vérifier que le stock a été mis à jour
        assertEquals(150, inventory.getQtyOnHand()); // 100 (initial) + 50 (requête)
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testHandleMovement_OUTBOUND_Success() {
        // Arrange
        request.setType(MovementType.OUTBOUND);
        request.setQuantity(50); // 50 demandés, 80 dispo (100 - 20)
        when(inventoryRepository.findByProductAndWarehouse(product, warehouse)).thenReturn(Optional.of(inventory));

        // Act
        inventoryService.handleStockMovement(request);

        // Assert
        assertEquals(50, inventory.getQtyOnHand()); // 100 (initial) - 50 (requête)
        verify(inventoryRepository, times(1)).save(inventory);
    }

    @Test
    void testHandleMovement_OUTBOUND_Fails_StockUnavailable() {
        // Arrange
        request.setType(MovementType.OUTBOUND);
        request.setQuantity(90); // 90 demandés, 80 dispo (100 - 20)
        when(inventoryRepository.findByProductAndWarehouse(product, warehouse)).thenReturn(Optional.of(inventory));

        // Act & Assert
        // (US7: Interdiction stock négatif)
        StockUnavailableException ex = assertThrows(StockUnavailableException.class, () -> {
            inventoryService.handleStockMovement(request);
        });

        assertTrue(ex.getMessage().contains("Stock unavailable"));
        // Vérifier qu'on n'a RIEN sauvegardé
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void testHandleMovement_ADJUSTMENT_Fails_BelowReserved() {
        // Arrange
        request.setType(MovementType.ADJUSTMENT);
        request.setQuantity(10); // Ajustement à 10. Réservé = 20
        when(inventoryRepository.findByProductAndWarehouse(product, warehouse)).thenReturn(Optional.of(inventory));

        // Act & Assert
        // (US8: Ne jamais tomber sous le réservé)
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            inventoryService.handleStockMovement(request);
        });

        assertTrue(ex.getMessage().contains("would be less than reserved quantity"));
        verify(inventoryRepository, never()).save(any());
    }
}