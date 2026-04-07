package com.oz.product.service;

import com.oz.common.dto.PageResponse;
import com.oz.common.exception.CustomException;
import com.oz.common.executors.CustomThreadPoolExecutor;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.entity.Product;
import com.oz.product.enums.SortField;
import com.oz.product.enums.TypeOfThing;
import com.oz.product.mapper.ProductMapper;

import com.oz.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Cacheable(value = "products",
            key = "{#page, #size, #sortFields, #direction}",
            unless = "#result == null", sync=true)
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
}

