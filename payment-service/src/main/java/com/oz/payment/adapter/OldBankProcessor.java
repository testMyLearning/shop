package com.oz.payment.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
@Primary
public class OldBankProcessor implements BankProcessor {
    @Override
    public boolean checkBalance(String userId, BigDecimal balance) {
        try {
            log.info("Проверка баланса в старом банке");
            Thread.sleep(1000);
return Math.random() > 0.2;
        } catch (InterruptedException e) {
            return false;
        }
    }
}
