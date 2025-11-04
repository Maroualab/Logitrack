package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.dto.product.ProductDTO;
import com.logitrack.logitrack.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController
{

    private final ProductService productService;

    @PostMapping("/create")
    public String createProduct(@Valid @RequestBody ProductDTO productDTO){
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return "Product created with the name : " + createdProduct.getName();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("{id}")
    public String updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO){
        productService.updateProduct(id, productDTO);
        return "Product updated with id: " + id;
    }

    @PatchMapping("/deactivate/{id}")
    public String deactivateProduct(@PathVariable Long id){
        productService.deactiveteProduct(id);
        return "Product deactivated with id: " + id;
    }






}
