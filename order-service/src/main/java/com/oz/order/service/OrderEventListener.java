package com.oz.order.service;

import com.oz.common.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final OrderOperationService orderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Transaction committed. Sending event to Kafka for order: {}", event.getOrderId());

        // Теперь отправка безопасна: данные точно есть в БД
        kafkaTemplate.send("order-created", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Доставлено в топик: {}, партиция: {}, офсет: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                        // Здесь можно реализовать логику досылки или отмены заказа
                    } else {
                        log.error("Failed to send Kafka message for order {}", event.getOrderId(), ex);
                        orderService.cancelOrder(event.getOrderId(),"Ошибка отправки сообщения в кафку на оплату");

                    }
                });
    }
}