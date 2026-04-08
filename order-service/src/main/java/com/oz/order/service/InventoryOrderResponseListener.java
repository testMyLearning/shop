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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InventoryOrderResponseListener {

    private final OrderOperationService orderOperationService;
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "inventory-reserved")
    @Transactional
    public void handleInventoryReserved(InventoryReservedEvent event) {
        Order order  = orderRepository.findById(event.orderId())
                .orElseThrow(() -> new CustomException("Заказ не найден в базе!"));
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
        orderOperationService.cancelOrder(event.id(),"OUT_OF_STOCK");

    }

}
