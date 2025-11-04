package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.warehouse.WarehouseDTO;
import com.logitrack.logitrack.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

    @Mapping(source = "warehouseManager.id", target = "warehouseManagerId")
    WarehouseDTO toDto(Warehouse warehouse);

    @Mapping(target = "warehouseManager", ignore = true)
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    List<WarehouseDTO> toDtoList(List<Warehouse> warehouses);
}