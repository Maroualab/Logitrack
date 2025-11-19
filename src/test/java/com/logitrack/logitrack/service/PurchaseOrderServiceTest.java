package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.mapper.InventoryMapper;
import com.logitrack.logitrack.dto.inventory.InventoryDTO;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.model.enums.PurchaseOrderStatus;
import com.logitrack.logitrack.repository.PurchaseOrderLineRepository;
import com.logitrack.logitrack.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;
    @Mock
    private PurchaseOrderLineRepository purchaseOrderLineRepository;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private InventoryMapper inventoryMapper;

    // Note: 'createPurchaseOrder' est omis pour la brièveté,
    // mais devrait être testé de la même manière que 'createSalesOrder'

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    private PurchaseOrder purchaseOrder;
    private PurchaseOrderLine poLine;

    @BeforeEach
    void setUp() {
        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(1L);
        purchaseOrder.setStatus(PurchaseOrderStatus.APPROVED);

        Product product = new Product();
        product.setId(1L);

        poLine = new PurchaseOrderLine();
        poLine.setId(10L);
        poLine.setPurchaseOrder(purchaseOrder);
        poLine.setProduct(product);
        poLine.setQuantity(100); // 100 commandés
        poLine.setQuantityReceived(0); // 0 reçus
    }

    @Test
    void testReceiveItems_Success() {
        // Arrange (US14)
        Long warehouseId = 1L;
        int quantityToReceive = 50;

        when(purchaseOrderLineRepository.findById(10L)).thenReturn(Optional.of(poLine));
        // Simuler le check du statut (tout est reçu)
        when(purchaseOrderLineRepository.findAllByPurchaseOrderId(1L)).thenReturn(List.of(poLine));

        Inventory updatedInventory = new Inventory(); // Le stock retourné par le moteur
        when(inventoryService.handleStockMovement(any(MovementRequestDTO.class))).thenReturn(updatedInventory);
        when(inventoryMapper.toDto(updatedInventory)).thenReturn(new InventoryDTO());

        // Act
        InventoryDTO result = purchaseOrderService.receiveItems(10L, warehouseId, quantityToReceive);

        // Assert
        assertNotNull(result);
        // 1. Vérifie que la quantité reçue sur la ligne a été mise à jour
        assertEquals(50, poLine.getQuantityReceived());
        // 2. Vérifie que le moteur de stock a été appelé avec les bonnes infos
        verify(inventoryService).handleStockMovement(argThat(req ->
                req.getType() == MovementType.INBOUND &&
                        req.getQuantity() == 50 &&
                        req.getWarehouseId() == warehouseId &&
                        req.getReferenceDocument().equals("PO-1")
        ));
        // 3. Le statut ne doit PAS changer (réception partielle)
        assertEquals(PurchaseOrderStatus.APPROVED, purchaseOrder.getStatus());
    }

    @Test
    void testReceiveItems_Fails_QuantityTooHigh() {
        // Arrange (US14)
        Long warehouseId = 1L;
        int quantityToReceive = 110; // Tente de recevoir 110, mais 100 commandés

        when(purchaseOrderLineRepository.findById(10L)).thenReturn(Optional.of(poLine));

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            purchaseOrderService.receiveItems(10L, warehouseId, quantityToReceive);
        });

        assertTrue(ex.getMessage().contains("Cannot receive 110 items"));
        verify(inventoryService, never()).handleStockMovement(any());
    }
}