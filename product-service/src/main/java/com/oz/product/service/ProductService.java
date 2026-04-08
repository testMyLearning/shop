package com.oz.product.service;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.SliceResponse;
import com.oz.product.entity.Product;
import com.oz.product.enums.SortField;
import com.oz.product.mapper.ProductMapper;
import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Cacheable(value = "products",
            key = "{#page, #size, #sortFields, #direction}", sync = true)
    @Transactional(readOnly = true)
    public PageResponse<ProductDto> getAllProducts(int page,
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
        return productMapper.toPageResponse(productRepository.findAll(pageable));
    }
    @Cacheable(value = "products",
            key = "{#page, #size, #sortFields, #direction}",
            unless = "#result == null")
    @Transactional(readOnly = true)
    public SliceResponse<ProductDto> getAllProductsWithSlice(int page,
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

        // Используем PageRequest, но результат приводим к Slice
        Pageable pageable = PageRequest.of(page, size, sort);

        // findAll вернет Slice, если мы так скажем, или просто отсечет лишнее
        Slice<Product> productSlice = productRepository.findAll(pageable);

        return productMapper.toSliceResponse(productSlice);
    }
}

