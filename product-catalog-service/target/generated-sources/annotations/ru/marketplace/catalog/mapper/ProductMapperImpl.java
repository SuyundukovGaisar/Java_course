package ru.marketplace.catalog.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.marketplace.catalog.dto.ProductDto;
import ru.marketplace.catalog.model.Product;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-07T16:16:46+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDto toDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDto productDto = new ProductDto();

        productDto.setId( product.getId() );
        productDto.setCategory( product.getCategory() );
        productDto.setBrand( product.getBrand() );
        productDto.setPrice( product.getPrice() );

        return productDto;
    }

    @Override
    public Product toEntity(ProductDto dto) {
        if ( dto == null ) {
            return null;
        }

        Product product = new Product();

        if ( dto.getId() != null ) {
            product.setId( dto.getId() );
        }
        product.setCategory( dto.getCategory() );
        product.setBrand( dto.getBrand() );
        product.setPrice( dto.getPrice() );

        return product;
    }
}
