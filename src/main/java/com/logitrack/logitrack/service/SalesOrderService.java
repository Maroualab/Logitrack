package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.exception.StockUnavailableException;
import com.logitrack.logitrack.mapper.SalesOrderMapper;
import com.logitrack.logitrack.mapper.SalesOrderLineMapper;
import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.dto.salesOrder.SalesOrderDTO;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import com.logitrack.logitrack.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;

    private final SalesOrderMapper salesOrderMapper;
    private final SalesOrderLineMapper salesOrderLineMapper;


    @Transactional
    public SalesOrderDTO createSalesOrder(SalesOrderDTO salesOrderDTO) {
        Client client = clientRepository.findById(salesOrderDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        Warehouse warehouse = warehouseRepository.findById(salesOrderDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found"));

        SalesOrder salesOrder = salesOrderMapper.toEntity(salesOrderDTO);
        salesOrder.setClient(client);
        salesOrder.setWarehouse(warehouse);
        salesOrder.setStatus(SalesOrderStatus.CREATED);

        for (var lineDTO : salesOrderDTO.getOrderLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + lineDTO.getProductId()));

            SalesOrderLine line = salesOrderLineMapper.toEntity(lineDTO);
            line.setProduct(product);
            salesOrder.addLine(line);
        }

        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
        return salesOrderMapper.toDto(savedOrder);
    }


    @Transactional
    public SalesOrderDTO reserveOrder(Long orderId) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder not found"));

        if (order.getStatus() != SalesOrderStatus.CREATED) {
            throw new BusinessException("Order cannot be reserved. Status is " + order.getStatus());
        }

        Warehouse warehouse = order.getWarehouse();

        for (SalesOrderLine line : order.getOrderLines()) {
            Product product = line.getProduct();
            int quantityToReserve = line.getQuantity();

            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                    .orElseThrow(() -> new BusinessException("No stock found for product " + product.getSku()
                            + " in warehouse " + warehouse.getCode()));

            int availableQty = inventory.getQtyOnHand() - inventory.getQtyReserved();

            if (availableQty < quantityToReserve) {
                throw new StockUnavailableException("Stock unavailable for " + product.getSku()
                        + ". Available: " + availableQty + ", Requested: " + quantityToReserve);
            }

            inventory.setQtyReserved(inventory.getQtyReserved() + quantityToReserve);
            inventoryRepository.save(inventory);
        }

        order.setStatus(SalesOrderStatus.RESERVED);
        SalesOrder savedOrder = salesOrderRepository.save(order);
        return salesOrderMapper.toDto(savedOrder);
    }


    @Transactional
    public SalesOrderDTO shipOrder(Long orderId) {
        SalesOrder order = salesOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("SalesOrder not found"));

        if (order.getStatus() != SalesOrderStatus.RESERVED) {
            throw new BusinessException("Order cannot be shipped. Status is "+ order.getStatus());
        }

        Warehouse warehouse = order.getWarehouse();
        String reference = "SO-" + order.getId();

        for (SalesOrderLine line : order.getOrderLines()) {
            Product product = line.getProduct();
            int quantityToShip = line.getQuantity();


            Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                    .orElseThrow(() -> new BusinessException("Inventory record disappeared for " + product.getSku()));

            inventory.setQtyReserved(inventory.getQtyReserved() - quantityToShip);
            inventoryRepository.save(inventory);


            MovementRequestDTO movement = new MovementRequestDTO();
            movement.setProductId(product.getId());
            movement.setWarehouseId(warehouse.getId());
            movement.setQuantity(quantityToShip);
            movement.setType(MovementType.OUTBOUND);
            movement.setReferenceDocument(reference);

            inventoryService.handleStockMovement(movement);
        }

        order.setStatus(SalesOrderStatus.SHIPPED);
        SalesOrder savedOrder = salesOrderRepository.save(order);
        return salesOrderMapper.toDto(savedOrder);
    }

    public List<SalesOrderDTO> AllReservedSalesOrders(){
        List<SalesOrder> reservedSalesOrders = salesOrderRepository.findAllByStatus(SalesOrderStatus.RESERVED);

        return reservedSalesOrders.stream().map(salesOrderMapper::toDto).toList();

}

}
