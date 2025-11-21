package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.PurchaseOrderMapper;
import com.logitrack.logitrack.mapper.PurchaseOrderLineMapper;
import com.logitrack.logitrack.dto.inventory.InventoryDTO;
import com.logitrack.logitrack.dto.MovementRequestDTO;
import com.logitrack.logitrack.dto.purchaseOrder.PurchaseOrderDTO;
import com.logitrack.logitrack.dto.purchaseOrder.PurchaseOrderLineDTO;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.model.enums.MovementType;
import com.logitrack.logitrack.model.enums.PurchaseOrderStatus;
import com.logitrack.logitrack.repository.PurchaseOrderLineRepository;
import com.logitrack.logitrack.repository.PurchaseOrderRepository;
import com.logitrack.logitrack.repository.ProductRepository;
import com.logitrack.logitrack.repository.SupplierRepository;
import com.logitrack.logitrack.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderLineRepository purchaseOrderLineRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderLineMapper purchaseOrderLineMapper;

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;


    @Transactional
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) {

        Supplier supplier = supplierRepository.findById(purchaseOrderDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + purchaseOrderDTO.getSupplierId()));

        PurchaseOrder purchaseOrder = purchaseOrderMapper.toEntity(purchaseOrderDTO);
        purchaseOrder.setSupplier(supplier);
        purchaseOrder.setStatus(PurchaseOrderStatus.CREATED);
        purchaseOrderDTO.getOrderLines().stream().forEach(
                lineDTO -> {Product product = productRepository.findById(lineDTO.getProductId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lineDTO.getProductId()));

                    PurchaseOrderLine lineEntity = purchaseOrderLineMapper.toEntity(lineDTO);
                    lineEntity.setProduct(product);
                    purchaseOrder.addLine(lineEntity);
                });

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }




    @Transactional
    public InventoryDTO receiveItems(Long poLineId, Long warehouseId) {

        PurchaseOrderLine poLine = purchaseOrderLineRepository.findById(poLineId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrderLine not found with id: " + poLineId));

        PurchaseOrder purchaseOrder = poLine.getPurchaseOrder();


        if (purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELED ||
                purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new BusinessException("Cannot receive items for a PO that is " + purchaseOrder.getStatus());
        }

        int remainingQuantity = poLine.getQuantity() - poLine.getQuantityReceived();
        
        if (remainingQuantity <= 0) {
            throw new BusinessException("All items from this PO line have already been received.");
        }


        MovementRequestDTO movementRequest = new MovementRequestDTO();
        movementRequest.setProductId(poLine.getProduct().getId());
        movementRequest.setWarehouseId(warehouseId);
        movementRequest.setQuantity(remainingQuantity);
        movementRequest.setType(MovementType.INBOUND);
        movementRequest.setReferenceDocument("PO-" + purchaseOrder.getId());

        Inventory updatedInventory = inventoryService.handleStockMovement(movementRequest);

        poLine.setQuantityReceived(poLine.getQuantityReceived() + remainingQuantity);
        purchaseOrderLineRepository.save(poLine);

        checkAndUpdatePOStatus(purchaseOrder);

        return inventoryMapper.toDto(updatedInventory);
    }

    private void checkAndUpdatePOStatus(PurchaseOrder purchaseOrder) {
        List<PurchaseOrderLine> lines = purchaseOrderLineRepository.findAllByPurchaseOrderId(purchaseOrder.getId());

        boolean allLinesCompleted = lines.stream()
                .allMatch(line -> line.getQuantity() == line.getQuantityReceived());

        if (allLinesCompleted) {
            purchaseOrder.setStatus(PurchaseOrderStatus.RECEIVED);
            purchaseOrderRepository.save(purchaseOrder);
        }
    }



    @Transactional(readOnly = true)
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrders.stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public PurchaseOrderDTO cancelPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder not found with id: " + purchaseOrderId));

        if (purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new BusinessException("Cannot cancel a purchase order that has been fully received.");
        }

        if (purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELED) {
            throw new BusinessException("This purchase order is already canceled.");
        }

        purchaseOrder.setStatus(PurchaseOrderStatus.CANCELED);
        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toDto(savedPurchaseOrder);
    }

}