package com.oz.cart.service;

import com.oz.cart.aop.LockableCart;
import com.oz.cart.dto.AddToCartRequest;
import com.oz.cart.dto.ResponseCart;
import com.oz.cart.entity.Cart;
import com.oz.cart.mapper.CartMapper;
import com.oz.cart.repository.CartRepository;
import com.oz.product.dto.ProductDto;
import com.oz.product.entity.Product;
import com.oz.product.service.ProductService;
import com.oz.product.service.ProductServiceOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductServiceOperation productServiceOperation;

    public void SaveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public ResponseCart getCart(String id) {
        Cart cart = cartRepository.findById(id).orElseGet(() -> new Cart(id, new ArrayList<>()));
        return cartMapper.toResponse(cart);
    }

    @LockableCart(cartId = "id")
    public void addProductToCart(String id, AddToCartRequest request) {
        Product product = productServiceOperation.getOneByID(request.productId());
        Cart cart = cartRepository.findById(id).orElseGet(() -> new Cart(id, new ArrayList<>()));
        ProductDto itemToAdd = cartMapper.toProductDto(product, request.quantity());


        cart.addItem(itemToAdd);

        cartRepository.save(cart);

    }
}

