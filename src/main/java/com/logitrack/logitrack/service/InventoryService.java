package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.exception.StockUnavailableException;
import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.model.Inventory;
import com.logitrack.logitrack.model.InventoryMovement;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.model.Warehouse;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.repository.InventoryMovementRepository;
import com.logitrack.logitrack.repository.InventoryRepository;
import com.logitrack.logitrack.repository.ProductRepository;
import com.logitrack.logitrack.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {


    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;


    @Transactional
    public Inventory handleStockMovement(MovementRequestDTO request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + request.getWarehouseId()));

        Inventory inventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                .orElseGet(() -> createNewInventory(product, warehouse));

        int movementQuantity = request.getQuantity();

        switch (request.getType()) {
            case INBOUND:
                inventory.setQtyOnHand(inventory.getQtyOnHand() + movementQuantity);
                break;

            case OUTBOUND:
                handleOutbound(inventory, movementQuantity);
                break;

            case ADJUSTMENT:
                handleAdjustment(inventory, movementQuantity);
                break;
        }

        Inventory savedInventory = inventoryRepository.save(inventory);

        // Log the movement
        logMovement(product, warehouse, request.getType(), movementQuantity, request.getReferenceDocument());

        return savedInventory;
    }


    private Inventory createNewInventory(Product product, Warehouse warehouse) {
        Inventory newInventory = new Inventory();
        newInventory.setProduct(product);
        newInventory.setWarehouse(warehouse);
        newInventory.setQtyOnHand(0);
        newInventory.setQtyReserved(0);
        return newInventory;
    }


    private void handleOutbound(Inventory inventory, int quantityToShip) {
        int availableQuantity = inventory.getQtyOnHand() - inventory.getQtyReserved();

        if (availableQuantity < quantityToShip) {
            throw new StockUnavailableException("Stock unavailable for product " + inventory.getProduct().getSku()
                    + ". Available: " + availableQuantity
                    + ", Requested: " + quantityToShip);
        }
        inventory.setQtyOnHand(inventory.getQtyOnHand() - quantityToShip);
    }


    private void handleAdjustment(Inventory inventory, int newPhysicalQuantity) {
        if (newPhysicalQuantity < inventory.getQtyReserved()) {
            throw new BusinessException("Adjustment failed. New quantity (" + newPhysicalQuantity
                    + ") would be less than reserved quantity ("
                    + inventory.getQtyReserved() + ").");
        }
        inventory.setQtyOnHand(newPhysicalQuantity);
    }


    private void logMovement(Product product, Warehouse warehouse, MovementType type, int quantity, String referenceDocument) {
        InventoryMovement movement = new InventoryMovement();
        movement.setProduct(product);
        movement.setWarehouse(warehouse);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setReferenceDocument(referenceDocument);
        movementRepository.save(movement);
    }
}