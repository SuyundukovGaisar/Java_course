package ru.marketplace.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.model.Product;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    Product toEntity(ProductDto dto);
}