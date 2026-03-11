package com.oz.product.service;

import com.oz.common.dto.PageResponse;
import com.oz.product.dto.ProductDto;
import com.oz.product.dto.UpdateProductDto;
import com.oz.product.entity.Product;
import com.oz.product.enums.SortField;
import com.oz.product.mapper.ProductMapper;
import com.oz.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;


    @Transactional(readOnly = true)
    public PageResponse<ProductDto> getAllProducts(int page,
                                                   int size,
                                                   SortField[] sortField,
                                                   SortField.Direction direction) {
        PageResponse<ProductDto> checkCache = checkFromCache(page,size,sortField ,direction);
        if(checkCache!=null){
            return checkCache;
        }

    Sort sort = this.sortingBy(sortField,direction);
    Pageable pageable = PageRequest.of(page,size,sort);
    Page<Product> resultPage = productRepository.findAll(pageable);
    PageResponse<ProductDto> response = convert(resultPage);
        CompletableFuture.runAsync(() -> {
            redisTemplate.opsForValue().set(this.saveCachedKey(page, size, sortField, direction),
                    response,
                    10, TimeUnit.SECONDS);
        });
    return response;

    }
    private String saveCachedKey(int page,
                                 int size,
                                 SortField[] sortField,
                                 SortField.Direction direction){
        return String.format("product:%d,%d,%s,%s",
                page,size,Arrays.toString(sortField), direction.name());
    }
    private PageResponse<ProductDto> checkFromCache(int page,
                                                    int size,
                                                    SortField[] sortField,
                                                    SortField.Direction direction){
        String key=saveCachedKey(page, size, sortField, direction);
        Object cached = redisTemplate.opsForValue().get(key);
        return cached != null ? (PageResponse<ProductDto>) cached : null;
    }


    private Sort sortingBy(SortField[] sortField, SortField.Direction direction){
     return Sort.by(Arrays.stream(sortField)
             .map(field -> new Sort.Order(Sort.Direction.fromString(direction.name()),field.name()))
             .toList());
    }

    private PageResponse<ProductDto> convert(Page<Product> page){
        PageResponse<ProductDto> result = new PageResponse<>();
        result.setResponseBody(productMapper.toDtoList(page.getContent()));
        result.setSize(page.getSize());
        result.setPage(page.getNumber());
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        result.setFirstPage(page.isFirst());
        result.setLastPage(page.isLast());
        return result;

    }

    @Transactional()
    public void createProduct(@Valid ProductDto productDto) {
        Product entity = productMapper.toEntity(productDto);
        productRepository.save(entity);
        clearRedis();
    }
    @Transactional
    public void deleteByProductId(UUID uuid) {
        productRepository.deleteById(uuid);
        clearRedis();
    }

    @Transactional
    public void updateProducts(@Valid UpdateProductDto productDto, UUID id) {
        Product findProduct = productRepository.findByIdWithLock(id)
                .orElseThrow(()-> new RuntimeException("Не найден продукт"));
        productMapper.updateProductFromDTO(productDto,findProduct);
        clearRedis();
    }

    private void clearRedis(){
        redisTemplate.keys("product:*").forEach(k->{
            redisTemplate.delete(k);
        });
    }
}
