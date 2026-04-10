package com.oz.order.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class FutureService {

    private final Map<UUID, CompletableFuture<ResponseEntity<String>>> futureMap = new ConcurrentHashMap<>();

    // МЕТОД РЕГИСТРАЦИИ
    public void register(UUID orderId, CompletableFuture<ResponseEntity<String>> future) {
        futureMap.put(orderId, future);
    }

    // МЕТОД ЗАВЕРШЕНИЯ (вызывается из Kafka Listener)
    public void complete(UUID orderId, ResponseEntity<String> response) {
        CompletableFuture<ResponseEntity<String>> future = futureMap.remove(orderId);
        if (future != null) {
            future.complete(response); // Пробуждает виртуальный поток в контроллере
        }
    }

    public void remove(UUID id) {
        if (id != null) {
            futureMap.remove(id);
        }
    }
}
