package com.oz.product.service;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.ProductFilter;
import com.oz.product.dto.SliceResponse;
import com.oz.product.entity.Product;
import com.oz.product.enums.SortField;
import com.oz.product.mapper.ProductMapper;
import com.oz.product.repository.ProductRepository;
import com.oz.product.specifications.ProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ServiceSpecifications {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
@Transactional(readOnly = true)
    public PageResponse<ProductDto> findProducts(
            ProductFilter filter,int page,
            int size,
            SortField[] sortFields,
            SortField.Direction direction) {
        Sort sort = Sort.by(
                Arrays.stream(sortFields)
                        .map(field -> new Sort.Order(
                                Sort.Direction.fromString(direction.name()),
                                field.name()
                        ))
                        .toList()
        );
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasType(filter.type()))
                .and(ProductSpecifications.priceLessThan(filter.maxPrice()))
                .and(ProductSpecifications.hasMinQuantity(filter.minCount()))
                .and(ProductSpecifications.hasColor(filter.color()));
        Page<Product> productSlice = productRepository.findAll(spec, pageable);
        return productMapper.toPageResponse(productSlice);
    }
}
