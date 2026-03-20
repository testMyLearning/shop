package com.oz.payment.adapter;

import java.math.BigDecimal;

public interface BankProcessor {
    boolean checkBalance(String userId, BigDecimal balance);
}
