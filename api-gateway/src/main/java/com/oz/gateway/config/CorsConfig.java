package com.oz.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.beans.BeanProperty;
import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    @Order(-1)  // Важно: CORS должен быть самым первым фильтром!
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // Разрешаем все необходимые источники
        config.addAllowedOrigin("http://localhost:3000");  // React dev
        config.addAllowedOrigin("http://localhost:8080");  // сам Gateway (для тестов)
        config.addAllowedOrigin("http://127.0.0.1:3000");  // через IP тоже

        // Если будете деплоить, добавьте продакшен домены
        // config.addAllowedOrigin("https://shop.example.com");

        config.addAllowedHeader("*");  // все заголовки
        config.addAllowedMethod("*");  // все методы (GET, POST, PUT, DELETE...)

        // Заголовки, которые фронтенд может читать в ответе
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "X-User-Id",
                "X-User-Roles",
                "Content-Type"
        ));

        // Кэширование preflight запросов на 30 минут
        config.setMaxAge(1800L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // для всех путей

        return new CorsWebFilter(source);
    }
}
