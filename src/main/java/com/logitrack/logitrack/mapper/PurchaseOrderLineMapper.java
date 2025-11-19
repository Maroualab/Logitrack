package com.logitrack.logitrack.mapper;


import com.logitrack.logitrack.dto.purchaseOrder.PurchaseOrderLineDTO;
import com.logitrack.logitrack.model.PurchaseOrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderLineMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "quantityReceived", target = "quantityReceived")
    PurchaseOrderLineDTO toDto(PurchaseOrderLine entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "purchaseOrder", ignore = true)
    @Mapping(target = "quantityReceived", ignore = true)
    PurchaseOrderLine toEntity(PurchaseOrderLineDTO dto);
}