package com.oz.payment.adapter;

import com.oz.common.exception.CustomException;
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
        log.info("Проверка баланса в старом банке");
        boolean result = Math.random() > 0.2;
        if (result) {
            return true;
        } else {
            log.warn("Ошибка при проверки баланса из-за рандомайзера");
            return false;
        }

    }
}
