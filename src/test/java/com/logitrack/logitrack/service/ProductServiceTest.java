package com.logitrack.logitrack.service;

import com.logitrack.logitrack.exception.BusinessException;
import com.logitrack.logitrack.exception.ResourceNotFoundException;
import com.logitrack.logitrack.mapper.ProductMapper;
import com.logitrack.logitrack.dto.product.ProductDTO;
import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductById_Success() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        //when(productMapper.toDto(product)).thenReturn(productDTO);

        // Act
        ProductDTO result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetProductById_Fails_NotFound() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(99L);
        });
    }

    @Test
    void testCreateProduct_Fails_SkuExists() {
        // Arrange
        ProductDTO dto = new ProductDTO();
        dto.setSku("SKU-123");

        when(productRepository.findBySku("SKU-123")).thenReturn(Optional.of(new Product()));

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            productService.createProduct(dto);
        });
    }
}