package com.logitrack.logitrack.service;

import com.logitrack.logitrack.model.Product;
import com.logitrack.logitrack.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public String checkRepository() {
        try {
            long productCount = productRepository.count();
            return "Product repository is working! Total products: " + productCount;
        } catch (Exception e) {
            return "Product repository error: " + e.getMessage();
        }
    }
}