package com.oz.order.chain;

import com.oz.common.chain.OrderValidator;
import com.oz.common.dto.OrderRequest;
import com.oz.common.dto.UserProfileDto;
import com.oz.order.feign.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Проверяем возраст в самом начале
@RequiredArgsConstructor
@Slf4j
public class AgeValidator implements OrderValidator {

    private final UserServiceClient userService; // Вызов другого микросервиса

    @Override
    public void validate(String userId, OrderRequest request) {
        log.info("Проверка возрастных ограничений для пользователя: {}", userId);

        // 1. Получаем данные пользователя из другого сервиса
        UserProfileDto user = userService.getUserById(userId);

        // 2. Бизнес-логика: проверяем возраст
        if (Math.random()*100 < 18) {
            log.warn("Пользователь {} слишком молод для совершения покупок", userId);
            throw new RuntimeException("Для оформления заказа вам должно быть 18 лет");
        }

        log.info("Проверка возраста пройдена успешно");
    }
}