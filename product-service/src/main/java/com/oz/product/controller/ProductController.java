package com.oz.product.controller;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.enums.SortField;
import com.oz.product.service.ProductService;
import com.oz.product.service.ProductServiceOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.UUID;


@RestController
@RequestMapping("/api/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductServiceOperation productServiceOperation;

    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") SortField[] sortField,
            @RequestParam(defaultValue = "asc") SortField.Direction direction
    ) {
        PageResponse<ProductDto> response = productService.getAllProducts(page,size,sortField,direction);

        return ResponseEntity.ok(response);

    }

    @PostMapping
    @PreAuthorize("hasRole('seller')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        productServiceOperation.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") UUID uuid) {
        productServiceOperation.deleteByProductId(uuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductDto productDto,
                                           @PathVariable UUID id) {
        productServiceOperation.updateProducts(productDto, id);
        return ResponseEntity.ok().build();
    }




}
