package com.oz.product.collections;

import com.oz.common.aop.LogExecutionTime;
import com.oz.common.enums.Color;
import com.oz.product.entity.Product;
import com.oz.product.enums.TypeOfThing;
import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CollectionStressTestService {

    private final CollectionMetricsExecutor executor;

    public void runStressTest() {
        log.info("🚀 Запуск глобального стресс-теста коллекций (50 000 элементов)");

        List<Product> sourceData = new ArrayList<>();
        for (int i = 0; i < 50000; i++) {
            Product p = new Product();
            p.setId(UUID.randomUUID());
            p.setPrice(BigDecimal.valueOf(Math.random() * 10000));
            // Имитируем разные типы для фильтрации
            p.setTypeOfThing(i % 2 == 0 ? TypeOfThing.Jacket : TypeOfThing.Cap);
            sourceData.add(p);
        }

        // Запускаем все тесты на одном наборе данных
        executor.testArrayList(sourceData);
        log.info("testArrayList");
        executor.testHashSet(sourceData);
        executor.testPriorityQueue(sourceData);
        executor.testTreeSet(sourceData);
        executor.myTestArrayList(sourceData);
        executor.findProductLinkedlist(sourceData);
        executor.getUniqueProductsHashSet(sourceData);
        executor.getUniqueProductsLinkedSet(sourceData);
        executor.getProductMap(sourceData);
        executor.getSortedByPriceTreeMap(sourceData);
        executor.getExpensiveFirstQueue(sourceData);
        executor.getProductStack(sourceData);
    }


}