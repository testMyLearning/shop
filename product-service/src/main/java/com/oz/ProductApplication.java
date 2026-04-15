package com.oz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication(scanBasePackages = {"com.oz.common", "com.oz.product","com.oz.cart"})
@EnableKafka
@EnableJpaRepositories(basePackages = "com.oz")
@EnableRedisRepositories(basePackages = "com.oz.cart")
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }
}
