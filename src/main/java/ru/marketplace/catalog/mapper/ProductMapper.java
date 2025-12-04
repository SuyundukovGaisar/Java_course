package ru.marketplace.catalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.model.Product;

/**
 * Маппер для преобразования сущности Product в DTO и обратно.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toEntity(ProductDto dto);
}