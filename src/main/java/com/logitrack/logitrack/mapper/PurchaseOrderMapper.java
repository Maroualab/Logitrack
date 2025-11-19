package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.purchaseOrder.PurchaseOrderDTO;
import com.logitrack.logitrack.model.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { PurchaseOrderLineMapper.class })
public interface PurchaseOrderMapper {

    @Mapping(source = "supplier.id", target = "supplierId")
    @Mapping(source = "orderLines", target = "orderLines")
    PurchaseOrderDTO toDto(PurchaseOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    PurchaseOrder toEntity(PurchaseOrderDTO dto);
}