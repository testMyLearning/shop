package com.oz.common.dto;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public record PaymentFailed(UUID uuid,UUID productId, int quantity) {
}
