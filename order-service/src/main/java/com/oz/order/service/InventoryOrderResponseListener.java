package com.oz.order.service;

import com.oz.common.dto.InventoryFailedEvent;
import com.oz.common.dto.InventoryReservedEvent;
import com.oz.common.dto.PaymentRequestEvent;
import com.oz.order.dto.Order;
import com.oz.order.enums.OrderStatus;
import com.oz.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryOrderResponseListener {

    private final OrderOperationService orderOperationService;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @KafkaListener(topics = "inventory-reserved")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        orderOperationService.updateOrderStatus(event.orderId(), OrderStatus.INVENTORY_RESERVED);
        Order order  = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new RuntimeException("Заказ не найден в базе!"));
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
    public void handleInventoryFailed(InventoryFailedEvent event) {
        orderOperationService.cancelOrder(event.id(),"Отменен по причине: "+event.cause());

    }
    @KafkaListener(topics = "payment-success")
    public void handlePaymentSuccess(PaymentRequestEvent event){
        orderOperationService.updateOrderStatus(event.orderId(),OrderStatus.COMPLETED);
    }
//    @KafkaListener(topics = "payment-failed")
//    public void handlePaymentFailed(PaymentFailedEvent event) {
//        log.error("Оплата не прошла. Отменяем заказ: {}", event.orderId());
//        orderOperationService.cancelOrder(event.orderId(), "Payment failed: " + event.reason());
//
//    }

}
