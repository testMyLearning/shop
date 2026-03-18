package com.oz.product.service;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.InventoryReservedEvent;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.product.entity.Product;
import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @KafkaListener(topics = "payment-processed")
    @Transactional
    public void handlePaymentProcessed(OrderCreatedEvent event) {
        log.info("Reserving inventory for order: {}", event.getOrderId());

        int updatedCount = productRepository.decrementStock(event.getProductId(),event.getQuantity());
        if(updatedCount > 0){
            eventPublisher.publishEvent(new InventoryReservedEvent(event.getOrderId()));
        } else {
            eventPublisher.publishEvent(new InventoryFailedEvent(event.getOrderId(), "Нет нужного количества товара"));
        }
    };
}