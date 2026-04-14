package com.oz.cart.dto;

import java.util.UUID;

public record AddToCartRequest(UUID productId, long quantity) {
}
