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
import com.oz.product.patterns.PrototypeProductReportGenerator;
import com.oz.product.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductFactory productFactory;
    private final PrototypeProductReportGenerator generator;
    private final ObjectProvider<PrototypeProductReportGenerator> reportGeneratorObjectProvider;
    private final CustomThreadPoolExecutor executor;
    private final TransactionTemplate transactionTemplate;



    public CompletableFuture<PageResponse<ProductDto>> getAllProducts(int page,
                                                   int size,
                                                   SortField[] sortField,
                                                   SortField.Direction direction) {
        return CompletableFuture.supplyAsync(()-> checkFromCache(page,size,sortField ,direction),executor.customExecutor())
                .thenCompose(cached->{
                    if(cached !=null){
            return CompletableFuture.completedFuture(cached);
        }
                    return CompletableFuture.supplyAsync(()->
transactionTemplate.execute(status->{
    Sort sort = sortingBy(sortField,direction);
    Pageable pageable = PageRequest.of(page,size,sort);
    return convert(productRepository.findAll(pageable));
    }),executor.customExecutor()).thenApply(response-> {
            redisTemplate.opsForValue().set(this.saveCachedKey(page, size, sortField, direction),
                    response,
                    10, TimeUnit.SECONDS);
                        return response;
            });


    });
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
        TypeOfThing typeOfThing = TypeOfThing.fromName(productDto.typeOfThing());
        Product createProduct = productFactory.createProductWithType(typeOfThing);
        productMapper.createNewProductFromDTO(productDto,createProduct);
        productRepository.save(createProduct);
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
                .orElseThrow(()-> new CustomException("Не найден продукт"));
        productMapper.updateProductFromDTO(productDto,findProduct);
        clearRedis();
    }
    public byte[] downloadAllProductsReport() {
        // 1. Достаем свежий, чистый экземпляр прототипа
        PrototypeProductReportGenerator generator = reportGeneratorObjectProvider.getObject();

        // 2. Берем данные из БД
        List<Product> allProducts = productRepository.findAll();

        // 3. Настраиваем прототип под конкретную задачу
        generator.setProducts(allProducts);

        // 4. Получаем результат
        return generator.generateCsvReport();
    }
    public byte[] downloadAllProductsReportWithProxy() {

        List<Product> allProducts = productRepository.findAll();


        generator.setProducts(allProducts);

        return generator.generateCsvReport();
    }


    private void clearRedis(){
        redisTemplate.keys("product:*").forEach(k->{
            redisTemplate.delete(k);
        });
    }
}
