package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.dto.product.ProductDTO;
import com.logitrack.logitrack.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDTO dto);

    @Mapping(source = "sku", target = "sku")
    ProductDTO toDTO(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product product);
}
