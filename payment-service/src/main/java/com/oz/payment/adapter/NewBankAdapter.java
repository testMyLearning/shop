package com.oz.payment.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class NewBankAdapter implements BankProcessor{
    private final NewBankApi newBankApi = new NewBankApi();
    @Override
    public boolean checkBalance(String userId, BigDecimal balance) {
        int cents = balance.multiply(new BigDecimal("100")).intValue();
        String response = newBankApi.verifyFunds(userId, cents);
        return "OK".equals(response);
    }
}
