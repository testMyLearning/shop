package com.oz.product.service;

import com.oz.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Semaphore;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServiceTestAddProducts {

    private final ProductRepository productRepository;
    private final Semaphore semaphore = new Semaphore(30);


    public void addProduct() {
        try {
            semaphore.acquire();
            int rows = productRepository.incrementCount();
            if (rows > 0) {
                log.info("Успешно добавлено");
            } else {
                log.warn("Лимит достигнут");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }

    }


}
