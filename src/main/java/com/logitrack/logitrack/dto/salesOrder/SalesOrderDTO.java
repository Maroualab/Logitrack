package com.logitrack.logitrack.dto.salesOrder;

import com.logitrack.logitrack.model.enums.SalesOrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class SalesOrderDTO {

    private Long id;

    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotNull(message = "Source Warehouse ID cannot be null")
    private Long warehouseId;

    private String clientName;
    private String warehouseCode;
    private SalesOrderStatus status;
    private LocalDateTime createdAt;

    @Valid
    @NotEmpty(message = "Sales order must have at least one line")
    private List<SalesOrderLineDTO> orderLines;
}