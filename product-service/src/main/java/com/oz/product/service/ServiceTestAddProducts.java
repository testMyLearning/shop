package com.oz.product.service;

import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceTestAddProducts {

    private final ProductRepository productRepository;


    @Transactional
    public void addProduct() {
        UUID randomId = productRepository.findRandomId();

        if (randomId != null) {
            int rows = productRepository.incrementCount(randomId);
            if (rows > 0) {
                log.info("Успешно добавлено");
            } else {
                log.warn("Лимит достигнут для {}", randomId);
            }
        }

    }
}
