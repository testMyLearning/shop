package com.oz.cart.entity;

import com.oz.product.dto.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
    @Data
    @NoArgsConstructor
    @RedisHash(value = "cart", timeToLive = 60*15)
    public class Cart {

        @Id
        private String id;

        private List<ProductDto> list;

        public Cart(String id, List<ProductDto> list) {
            this.id = id;
            this.list = list;
        }
        public void addItem(ProductDto newItem) {
            // Логика переехала сюда!
            this.list.stream()
                    .filter(p -> p.name().equals(newItem.name()))
                    .findFirst()
                    .ifPresentOrElse(
                            existing -> updateExisting(existing, newItem.count()),
                            () -> this.list.add(newItem)
                    );
        }

        private void updateExisting(ProductDto existing, Long additionalCount) {
            this.list.remove(existing);
            this.list.add(new ProductDto(
                    existing.name(),
                    existing.count() + additionalCount,
                    existing.price(),
                    existing.color(),
                    existing.typeOfThing()
            ));
        }
    }
