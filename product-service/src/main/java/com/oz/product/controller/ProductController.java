package com.oz.product.controller;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.enums.SortField;
import com.oz.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {
private final ProductService productService;
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "price") SortField[] sortField,
            @RequestParam(defaultValue = "asc") SortField.Direction direction
    ) {
    PageResponse<ProductDto> result = productService.getAllProducts(page,size,sortField,direction);
    return ResponseEntity.ok(result);

    }
}
