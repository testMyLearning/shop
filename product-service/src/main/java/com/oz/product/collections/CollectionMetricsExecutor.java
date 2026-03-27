package com.oz.product.collections;

import com.oz.common.aop.LogExecutionTime;
import com.oz.product.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
@Component
public class CollectionMetricsExecutor {
    @LogExecutionTime
    public void testArrayList(List<Product> data) {
        new ArrayList<>(data); // Копирование массива
    }

    @LogExecutionTime
    public void testHashSet(List<Product> data) {
        new HashSet<>(data); // Хеширование объектов
    }

    @LogExecutionTime
    public void testPriorityQueue(List<Product> data) {
        PriorityQueue<Product> queue = new PriorityQueue<>(Comparator.comparing(Product::getPrice));
        queue.addAll(data); // Построение двоичной кучи
    }

    @LogExecutionTime
    public void testTreeSet(List<Product> data) {
        TreeSet<Product> treeSet = new TreeSet<>(Comparator.comparing(Product::getId));
        treeSet.addAll(data); // Построение красно-черного дерева
    }

    @LogExecutionTime
    public List<Product> myTestArrayList(List<Product> data) {
        BigDecimal threshold = BigDecimal.valueOf(100);
        return data.stream()
                .filter(p -> p.getPrice().compareTo(threshold) > 0)
                .toList(); // Быстрый ArrayList под капотом
    }

    @LogExecutionTime
    public List<Product> findProductLinkedlist(List<Product> data) {
        return data.stream()
                .sorted(Comparator.comparing(Product::getId))
                .collect(Collectors.toCollection(LinkedList::new)); // Создание 50к объектов Node
    }

    @LogExecutionTime
    public Set<Product> getUniqueProductsHashSet(List<Product> data) {
        return data.stream()
                .filter(p -> p.getTypeOfThing() != null && p.getTypeOfThing().toString().contains("Ca"))
                .collect(Collectors.toSet());
    }

    @LogExecutionTime
    public Set<Product> getUniqueProductsLinkedSet(List<Product> data) {
        return new LinkedHashSet<>(data); // Сохранение порядка + уникальность
    }

    @LogExecutionTime
    public Map<UUID, Product> getProductMap(List<Product> data) {
        return data.stream()
                .collect(Collectors.toMap(Product::getId, p -> p, (a, b) -> a));
    }

    @LogExecutionTime
    public TreeMap<BigDecimal, Product> getSortedByPriceTreeMap(List<Product> data) {
        return data.stream()
                .collect(Collectors.toMap(
                        Product::getPrice,
                        p -> p,
                        (existing, replacement) -> existing,
                        TreeMap::new // Построение дерева по ценам
                ));
    }

    @LogExecutionTime
    public PriorityQueue<Product> getExpensiveFirstQueue(List<Product> data) {
        PriorityQueue<Product> queue = new PriorityQueue<>(
                Comparator.comparing(Product::getPrice).reversed()
        );
        queue.addAll(data);
        return queue;
    }

    @LogExecutionTime
    public Deque<Product> getProductStack(List<Product> data) {
        Deque<Product> stack = new ArrayDeque<>();
        data.forEach(stack::push); // Быстрая вставка в начало (LIFO)
        return stack;
    }
}
