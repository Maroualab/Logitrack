package com.logitrack.logitrack.service;

import com.logitrack.logitrack.dto.product.ProductDTO;
import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.ProductMapper;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import com.logitrack.logitrack.repository.InventoryRepository;
import com.logitrack.logitrack.repository.ProductRepository;
import com.logitrack.logitrack.repository.SalesOrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final SalesOrderLineRepository salesOrderLineRepository;
    private final InventoryRepository inventoryRepository;

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDtoList(products);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDTO(product);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productMapper.updateEntityFromDTO(productDTO, existingProduct);
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDTO(updatedProduct);
    }



    public void deactivateProduct(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));

    long activeOrdersCount = salesOrderLineRepository.countByProduct_SkuAndSalesOrder_StatusIn(sku,List.of(SalesOrderStatus.CREATED, SalesOrderStatus.RESERVED));
        if (activeOrdersCount > 0) {
            throw new BusinessException(
                    "Cannot deactivate product with SKU " + sku +
                            " as it is associated with " + activeOrdersCount + " active sales order(s)."
            );
        }

        long qtyReserved = inventoryRepository.findByProduct_Sku(sku).stream()
                .mapToLong(inventory -> inventory.getQtyReserved() != 0 ? inventory.getQtyReserved() : 0)
                .sum();
        if (qtyReserved > 0) {
            throw new BusinessException(
                    "Cannot deactivate product with SKU " + sku +
                            " as it has " + qtyReserved + " units reserved in inventory."
            );}

        if (!product.getActive()) {
            throw new BusinessException("Product with sku " + sku + " is already deactivated.");
        }

        product.setActive(false);
        productRepository.save(product);
    }



}