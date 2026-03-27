package com.oz.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)  // отключаем CSRF
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**","/api/users/register","/api/users/register/**").permitAll()  // разрешаем логин
                        .anyExchange().permitAll()  // пока все разрешено для теста
                )
                .build();
    }
}