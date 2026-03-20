package com.oz.common.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(UUID productId, BigDecimal price, int quantity ) {
}
