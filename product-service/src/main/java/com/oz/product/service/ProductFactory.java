package com.oz.product.service;

import com.oz.product.entity.*;
import com.oz.product.enums.TypeOfThing;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {
    public Product createProductWithType(TypeOfThing type) {
        return switch (type){
            case Jacket -> new JacketProduct();
            case Cap -> new CapProduct();
            case Tshirt -> new Tshirt();
            case Trousers -> new Trousers();
                default -> throw new IllegalArgumentException("Неизвестный тип товара");
        };
    }
}
