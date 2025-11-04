package com.logitrack.logitrack.mapper;


import com.logitrack.logitrack.dto.supplier.SupplierDTO;
import com.logitrack.logitrack.model.Supplier;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    SupplierDTO toDto(Supplier supplier);

    Supplier toEntity(SupplierDTO supplierDTO);

    List<SupplierDTO> toDtoList(List<Supplier> suppliers);
}