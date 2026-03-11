package com.oz.product.dto;

import com.oz.common.enums.Color;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductDto(
        @Nullable
        String name,
        @Nullable
        @PositiveOrZero(message="Количество должно быть больше 0")
        Long count,
        @Nullable
        @Positive(message ="Цена должна быть положительной")
        BigDecimal price,
        @Nullable
        Color color,
        @Nullable
        String typeOfThing
) {
}
