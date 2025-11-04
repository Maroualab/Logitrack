package com.logitrack.logitrack.dto.product;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 30, message = "Category must be at most 30 characters")
    private String category;
    

    @PositiveOrZero(message = "Original Unit Price must be zero or positive")
    private BigDecimal ogUnitPrice;


    private Boolean active = true;



}
