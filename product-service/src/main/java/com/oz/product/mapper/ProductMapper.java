package com.oz.product.mapper;

import com.oz.product.dto.ProductDto;
import com.oz.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "color", source = "c")
    @Mapping(target = "typeOfThing", expression = "java(product.getTypeOfThing().getName())")
    ProductDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfEntry", ignore = true)
    @Mapping(target = "typeOfThing", expression = "java(TypeOfThing.fromName(dto.typeOfThing()))")
    Product toEntity(ProductDto dto);

    List<ProductDto> toDtoList(List<Product> content);
}
