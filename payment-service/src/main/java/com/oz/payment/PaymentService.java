package com.oz.payment;


import com.oz.common.aop.LogExecutionTime;
import com.oz.common.dto.PaymentFailed;
import com.oz.common.dto.PaymentRequestEvent;


import com.oz.payment.adapter.BankProcessor;
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
    private final BankProcessor bankProcessor;
    @KafkaListener(topics = "payment-request")
    @Transactional
    @LogExecutionTime
    public void handleOrderCreated(PaymentRequestEvent event) {
        log.info("Оплата по заказу: {}", event.orderId());

        try {
            boolean isSuccess = bankProcessor.checkBalance(event.userId(),event.price());
            if (isSuccess) {
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
        } catch (RuntimeException e) {
            log.error("ошибка в обработке платежа по причине : {}", e.getMessage());
        }
    }
//    @Transactional(readOnly = true) // Важно! Сессия должна быть открыта
//    public void processHugeAmountOfData() {
//        try (Stream<Payment> paymentStream = repository.streamAllPayments()) {
//            // Здесь Java использует Итератор "под капотом"
//            paymentStream.forEach(payment -> {
//                // Обрабатываем по одному, не загружая 1 000 000 записей в RAM
////                Если внутри filter или map тебе нужно вызвать:
////                Внешний микросервис по API.
////                Сложную библиотеку (например, расчет траектории, криптографию или PDF-генератор).
////                Метод, который есть только в Java-коде твоего проекта.
////                Пример: filter(payment -> internalRiskService.isFraud(payment)) — база данных не знает о твоем InternalRiskService.
//                log.info("Processing payment: {}", payment.getId());
//            });
//        }
//    }

}
