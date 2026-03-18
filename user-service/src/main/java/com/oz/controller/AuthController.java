package com.oz.controller;

import com.oz.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
@Value("${keycloak.client.client-secret}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 1. Ваш контроллер шлет запрос в Keycloak
        String url = "http://keycloak:8082/realms/shop-realm/protocol/openid-connect/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "shop-client");
        body.add("client_secret", "4ksvyGu1eI2ydTdlxp6JtNavSkm51pbJ");  // confidential клиент
        body.add("username", request.username());
        body.add("password", request.password());

        HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(body, new HttpHeaders());

        // 2. Keycloak проверяет пароль и возвращает JWT
        ResponseEntity<Map> response = restTemplate.postForEntity(url, httpRequest, Map.class);

        // 3. Вы возвращаете тот же JWT клиенту
        return ResponseEntity.ok(response.getBody());
    }
}


