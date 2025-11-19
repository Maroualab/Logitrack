package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.salesOrder.SalesOrderDTO;
import com.logitrack.logitrack.model.SalesOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SalesOrderLineMapper.class })
public interface SalesOrderMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.name", target = "clientName")
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.code", target = "warehouseCode")
    SalesOrderDTO toDto(SalesOrder entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "orderLines", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SalesOrder toEntity(SalesOrderDTO dto);
}