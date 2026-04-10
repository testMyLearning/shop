package com.oz.product.service;

import com.oz.common.exception.CustomException;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.entity.Product;
import com.oz.product.enums.TypeOfThing;
import com.oz.product.mapper.ProductMapper;
import com.oz.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceOperation {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductFactory productFactory;

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void createProduct(@Valid ProductDto productDto) {
        TypeOfThing typeOfThing = TypeOfThing.fromName(productDto.typeOfThing());
        Product createProduct = productFactory.createProductWithType(typeOfThing);
        productMapper.createNewProductFromDTO(productDto, createProduct);
        productRepository.save(createProduct);

    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void deleteByProductId(UUID uuid) {
        productRepository.deleteById(uuid);

    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void updateProducts(@Valid UpdateProductDto productDto, UUID productId) {
        Product findProduct = productRepository.findByIdWithLock(productId)
                .orElseThrow(() -> new CustomException("Не найден продукт"));
        productMapper.updateProductFromDTO(productDto, findProduct);

    }

}
