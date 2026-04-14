package com.oz.order.service;

import com.oz.common.dto.OrderRequest;
import com.oz.common.exception.CustomException;
import com.oz.order.dto.Order;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Semaphore;


@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderOperationService orderOperationService;
    //private final UserContext userContext;
    // Можно реализовать через прокидывание контекста с данными изи JWT и использовать Например userContext.getUserId()
    private final Semaphore semaphore = new Semaphore(50);

    public UUID testSemaphore(String keycloakId, OrderRequest request) {
        try {
            semaphore.acquire();
            return orderOperationService.createOrder(keycloakId, request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
        return null;
    }


    public Order findById(UUID uuid) {
        return orderRepository.findByIdWithStatusCompleted(uuid)
                .orElseThrow(() -> new CustomException("ошибка в поиске заказа со статусом completed"));
    }
}
