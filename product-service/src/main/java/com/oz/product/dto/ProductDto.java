package com.oz.product.dto;

import com.oz.common.enums.Color;
import com.oz.product.enums.TypeOfThing;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;


public record ProductDto(
        @NotBlank(message = "Имя обязательно")
        String name,
        @PositiveOrZero(message="Количество должно быть больше 0")
        Long count,
        @NotNull
        @Positive(message ="Цена должна быть положительной")
        BigDecimal price,
        @NotNull
        Color color,
        @NotBlank(message= "Тип должен указываться: куртка, штаны, шапка, кепка")
        String typeOfThing
) {
}
