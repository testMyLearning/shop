package com.oz.order.service;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.InventoryReservedEvent;
import com.oz.common.dto.PaymentRequestEvent;
import com.oz.common.exception.CustomException;
import com.oz.order.dto.Order;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryOrderResponseListener {

    private final OrderOperationService orderOperationService;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FutureService futureService;

    @RetryableTopic(attempts="3",backoff = @Backoff(delay = 2000,multiplier=2.0,maxDelay = 10000L),
            include = {CustomException.class, RuntimeException.class}, // на какие ошибки ретраить
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "inventory-reserved")
    @Transactional
    public void handleInventoryReserved(InventoryReservedEvent event) {

                Order order  = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new CustomException("Заказ не найден в базе!"));

        if (order.getStatus() == OrderStatus.INVENTORY_RESERVED) {
            log.warn("Событие уже было обработано, скипаем: {}", event.orderId());
            return;
        }
        orderOperationService.updateOrderStatus(event.orderId(), OrderStatus.INVENTORY_RESERVED);
        PaymentRequestEvent paymentRequest = new PaymentRequestEvent(
                order.getId(),
                order.getUserId(),
                order.getTotalPrice(),
                order.getProductId(),
                order.getQuantity());
                kafkaTemplate.send("payment-request",event.orderId().toString(), paymentRequest);

        log.info("Запрос на оплату отправлен для заказа: {}", order.getId());

    }
    @KafkaListener(topics = "inventory-failed")
    @Transactional
    public void handleInventoryFailed(InventoryFailedEvent event) {
        log.info("[INVENTORY FAILED] Ошибка при резервировании товара. Закрытие заявки");
        orderOperationService.cancelOrder(event.orderId(),"OUT_OF_STOCK");
        futureService.complete(event.orderId(),ResponseEntity.badRequest().body("Товар закончился"));

    }
    @DltHandler
    public void handleDlt(InventoryReservedEvent event, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error("Событие {} окончательно провалено в топике {}. Требуется ручное вмешательство.", event, topic);
        // Здесь можно пометить заказ как ERROR в базе
//        TODO Когда-нибудь я допишу стратегию на ошибку кафки
    }

}
