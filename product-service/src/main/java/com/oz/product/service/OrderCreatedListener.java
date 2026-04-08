package com.oz.product.service;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.InventoryReservedEvent;
import com.oz.common.dto.OrderCreatedEvent;
import com.oz.common.dto.PaymentFailedEvent;
import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedListener {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;


    @KafkaListener(topics = "order-created")
    @Transactional
    public void handlePaymentProcessed(OrderCreatedEvent event) {
        log.info("Резервируем товары по заявке: {}", event.orderId());

        int updatedCount = productRepository.decrementStock(event.productId(),event.quantity());
        if(updatedCount > 0){
            eventPublisher.publishEvent(new InventoryReservedEvent(event.orderId(),event.productId()
                    ,event.quantity()));
        } else {
            eventPublisher.publishEvent(new InventoryFailedEvent(event.orderId()
                    ,event.productId()
                    ,event.quantity()
                    , "Нет нужного количества товара"));
        }
    };
    @KafkaListener(topics = "payment-failed")
    @Transactional
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.warn("Оплата заказа {} не удалась. Начинаем возврат товара на склад.", event.orderId());

        // 1. Достаем данные заказа (нам нужно знать productId и количество)
        // В идеале эти данные должны быть в самом событии PaymentFailedEvent,
        // чтобы не ходить лишний раз в БД.

        productRepository.incrementStock(event.productId(), event.quantity());

        log.info("Товар для заказа {} успешно возвращен в сток.", event.orderId());
    }

}