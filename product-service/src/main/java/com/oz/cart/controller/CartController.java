package com.oz.cart.controller;

import com.oz.cart.dto.AddToCartRequest;
import com.oz.cart.dto.ResponseCart;
import com.oz.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;


    @GetMapping
    public ResponseEntity<ResponseCart> getCart(@AuthenticationPrincipal Jwt principal){
        log.info("запрос на получение карточки, принципал: {}",principal.getSubject());
        String keycloakId = principal.getSubject();
        return ResponseEntity.ok(cartService.getCart(keycloakId));
    }

    @PostMapping
    public ResponseEntity<String> addProductToCart(@RequestBody AddToCartRequest request,
                                                   @AuthenticationPrincipal Jwt principal){
        String keycloakId = principal.getSubject();
        cartService.addProductToCart(keycloakId,request);
        return ResponseEntity.ok("Товар добавлен в корзину");

    }
}
