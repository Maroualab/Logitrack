package com.logitrack.logitrack.dto.purchaseOrder;


import com.logitrack.logitrack.model.enums.PurchaseOrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseOrderDTO {

    private Long id;

    @NotNull(message = "Supplier ID cannot be null")
    private Long supplierId;

    private PurchaseOrderStatus status;

    private LocalDateTime expectedDeliveryDate;

    @Valid
    @NotEmpty(message = "Purchase order must have at least one line")
    private List<PurchaseOrderLineDTO> orderLines;
}