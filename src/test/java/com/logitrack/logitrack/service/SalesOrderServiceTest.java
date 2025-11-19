package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.StockUnavailableException;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import com.logitrack.logitrack.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesOrderServiceTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private InventoryService inventoryService; // Le Moteur de Stock est mocké

    // Mocks pour la validation (ignorer les mappers pour la simplicité du test)
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private WarehouseRepository warehouseRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SalesOrderService salesOrderService;

    private SalesOrder order;
    private Inventory inventory;
    private Product product;

    @BeforeEach
    void setUp() {
        Client client = new Client();
        client.setId(1L);
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        product = new Product();
        product.setId(1L);
        product.setSku("TSHIRT");

        order = new SalesOrder();
        order.setId(1L);
        order.setClient(client);
        order.setWarehouse(warehouse);
        order.setStatus(SalesOrderStatus.CREATED);

        SalesOrderLine line = new SalesOrderLine();
        line.setProduct(product);
        line.setQuantity(10); // Demande 10
        line.setUnitPrice(BigDecimal.TEN);
        order.addLine(line);

        inventory = new Inventory();
        inventory.setQtyOnHand(100);
        inventory.setQtyReserved(0); // 100 dispo
    }

    @Test
    void testReserveOrder_Success() {
        // Arrange (US4)
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductAndWarehouse(any(Product.class), any(Warehouse.class)))
                .thenReturn(Optional.of(inventory));
        when(salesOrderRepository.save(any(SalesOrder.class))).thenReturn(order);

        // Act
        salesOrderService.reserveOrder(1L);

        // Assert
        // Vérifie que le stock a été réservé
        assertEquals(10, inventory.getQtyReserved());
        // Vérifie que le statut de la commande a changé
        assertEquals(SalesOrderStatus.RESERVED, order.getStatus());
        verify(inventoryRepository, times(1)).save(inventory);
        verify(salesOrderRepository, times(1)).save(order);
    }

    @Test
    void testReserveOrder_Fails_StockUnavailable() {
        // Arrange (US4)
        inventory.setQtyOnHand(5); // 5 dispo, 10 demandés
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductAndWarehouse(any(Product.class), any(Warehouse.class)))
                .thenReturn(Optional.of(inventory));

        // Act & Assert
        StockUnavailableException ex = assertThrows(StockUnavailableException.class, () -> {
            salesOrderService.reserveOrder(1L);
        });

        assertTrue(ex.getMessage().contains("Stock unavailable"));
        // Le statut ne doit pas changer
        assertEquals(SalesOrderStatus.CREATED, order.getStatus());
    }

    @Test
    void testShipOrder_Success() {
        // Arrange (US11)
        order.setStatus(SalesOrderStatus.RESERVED);
        inventory.setQtyReserved(10); // Stock déjà réservé

        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(inventoryRepository.findByProductAndWarehouse(any(Product.class), any(Warehouse.class)))
                .thenReturn(Optional.of(inventory));

        // Act
        salesOrderService.shipOrder(1L);

        // Assert
        // 1. Vérifie que la réservation est libérée
        assertEquals(0, inventory.getQtyReserved());
        // 2. Vérifie que le statut de la commande a changé
        assertEquals(SalesOrderStatus.SHIPPED, order.getStatus());
        // 3. Vérifie que le Moteur de Stock (Tâche 4) a été appelé pour la sortie
        verify(inventoryService, times(1)).handleStockMovement(
                argThat(req ->
                        req.getType() == MovementType.OUTBOUND &&
                                req.getQuantity() == 10
                )
        );
    }
}