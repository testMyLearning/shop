package com.oz.product.controller;

import com.oz.common.dto.PageResponse;
import com.oz.product.collections.CollectionStressTestService;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.enums.SortField;
import com.oz.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CollectionStressTestService collectionStressTestService;

    @GetMapping
    @PreAuthorize("hasAnyRole('seller', 'buyer')")
    public CompletableFuture<ResponseEntity<PageResponse<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") SortField[] sortField,
            @RequestParam(defaultValue = "asc") SortField.Direction direction
    ) {
        return productService.getAllProducts(page,size,sortField,direction)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e-> ResponseEntity.status(500).build());
    }

    @PostMapping
    @PreAuthorize("hasRole('seller')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") UUID uuid) {
        productService.deleteByProductId(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductDto productDto,
                                           @PathVariable UUID id) {
        productService.updateProducts(productDto, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public ResponseEntity<?> startTestCollection(){
        collectionStressTestService.runStressTest();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
