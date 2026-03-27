package com.oz.order.service;

import com.oz.common.dto.PaymentFailedEvent;
import com.oz.common.dto.PaymentRequestEvent;
import com.oz.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {
    private final OrderOperationService orderOperationService;
    @KafkaListener(topics = "payment-success")
    public void handlePaymentSuccess(PaymentRequestEvent event){
        orderOperationService.updateOrderStatus(event.orderId(), OrderStatus.COMPLETED);
        log.info("[{}] Заказ оплачен успешно.",event.orderId());
    }
    @KafkaListener(topics = "payment-failed")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.warn("Оплата не прошла. Отменяем заказ: {}", event.orderId());
        orderOperationService.cancelOrder(event.orderId(), "ERROR_PAYMENT");

    }

}
