package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.salesOrder.SalesOrderLineDTO;
import com.logitrack.logitrack.model.SalesOrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesOrderLineMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.sku", target = "productSku")
    SalesOrderLineDTO toDto(SalesOrderLine entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "salesOrder", ignore = true)
    SalesOrderLine toEntity(SalesOrderLineDTO dto);
}