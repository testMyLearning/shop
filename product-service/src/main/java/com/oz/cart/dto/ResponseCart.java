package com.oz.cart.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ResponseCart(Map<String, Long> productNamesAndCounts, BigDecimal totalPrice) {
}
