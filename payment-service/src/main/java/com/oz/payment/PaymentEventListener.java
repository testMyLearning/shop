package com.oz.payment;

import com.oz.common.dto.PaymentFailed;
import com.oz.common.dto.PaymentFailedEvent;
import com.oz.common.dto.PaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventListener {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSuccessPaymentEvent( PaymentRequestEvent payment) {

        kafkaTemplate.send("payment-success",payment);
        log.info("заказ оплачен и отправлен в топик payment-success");

    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentFailure(PaymentFailed internalEvent) {
        kafkaTemplate.send("payment-failed"
                , new PaymentFailedEvent(internalEvent.uuid(), "Ошибка в классе слушателя платежей"));
    }
}
