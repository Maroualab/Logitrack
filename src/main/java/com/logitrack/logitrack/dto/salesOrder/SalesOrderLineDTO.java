package com.logitrack.logitrack.dto.salesOrder;


import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SalesOrderLineDTO {

    private Long id;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    private String productSku;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Unit price cannot be null")
    private BigDecimal unitPrice;
}