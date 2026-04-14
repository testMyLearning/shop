package com.oz.cart.controller;

import com.oz.cart.dto.AddToCartRequest;
import com.oz.cart.dto.ResponseCart;
import com.oz.cart.entity.Cart;
import com.oz.cart.service.CartService;
import com.oz.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @GetMapping
    public ResponseEntity<ResponseCart> getCart(@AuthenticationPrincipal Jwt principal){
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
