package com.oz.payment;


import com.oz.common.dto.PaymentFailed;
import com.oz.common.dto.PaymentRequestEvent;

import com.oz.payment.Payment;
import com.oz.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {


    private final ApplicationEventPublisher eventPublisher;
    private final PaymentRepository repository;
    @KafkaListener(topics = "payment-request")
    @Transactional
    public void handleOrderCreated(PaymentRequestEvent event) {
        log.info("Оплата по заказу: {}", event.orderId());

        try {
            // 1. Проверяем баланс пользователя (здесь была бы логика)
            // 2. Списываем деньги
            log.info("проверяем баланс пользователя");
            Thread.sleep(2000);
            if (Math.random() > 0.2) {
                Payment payment = new Payment();
                payment.setOrderId(event.orderId());
                payment.setUserId(event.userId());
                payment.setPrice(event.price());
                payment.setProductId(event.productId());
                payment.setQuantity(event.quantity());
                repository.save(payment);
                eventPublisher.publishEvent(event);
                log.info("публикуем событие о успешной оплате");
            } else {
                log.info("публикуем событие о не успешной оплате");
                eventPublisher.publishEvent((new PaymentFailed(event.orderId(),event.productId(),event.quantity())));
            }
        } catch (InterruptedException e) {
            log.error("ошибка в обработке платежа по причине : {}", e.getMessage());
        }
    }

}
