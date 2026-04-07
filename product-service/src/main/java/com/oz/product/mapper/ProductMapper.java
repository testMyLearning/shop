package com.oz.product.mapper;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.entity.Product;
import com.oz.product.enums.TypeOfThing;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;


@Mapper(componentModel = "spring",imports = {TypeOfThing.class})
public interface ProductMapper extends BasePageMapper<Product,ProductDto>{
    @Mapping(target = "color", source = "color")
    @Mapping(target = "typeOfThing", expression = "java(product.getTypeOfThing().getName())")
    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> content);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "typeOfThing", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfEntry", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProductFromDTO(UpdateProductDto dto, @MappingTarget Product entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "typeOfThing", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfEntry", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void createNewProductFromDTO(ProductDto dto, @MappingTarget Product Entity);

    
}
