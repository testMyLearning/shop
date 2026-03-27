package com.oz.payment;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {


    @Bean
    public NewTopic paymentSuccessTopic() {
        return TopicBuilder.name("payment-success")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
