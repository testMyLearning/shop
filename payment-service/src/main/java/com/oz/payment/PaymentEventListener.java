package com.oz.payment;

import com.oz.common.dto.PaymentFailed;
import com.oz.common.dto.PaymentFailedEvent;
import com.oz.common.dto.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @EventListener
    public void handleSuccessPaymentEvent( PaymentRequestEvent payment) {

        kafkaTemplate.send("payment-success",payment.orderId().toString(),payment);
        log.info("заказ оплачен и отправлен в топик payment-success");

    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentFailure(PaymentFailed event) {
        kafkaTemplate.send("payment-failed",event.uuid().toString()
                , new PaymentFailedEvent(event.uuid(),
                        event.productId(),
                        event.quantity(),
                        "Ошибка в классе слушателя платежей"));
    }
}
