package com.oz.common.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

//@Configuration
//public class KafkaConfig {
//    @Bean
//    public ProducerFactory<String, Object> producerFactory(KafkaProperties properties) {
//        // buildProducerProperties() вытаскивает все: acks, batch-size, serializers и т.д.
//        DefaultKafkaProducerFactory<String, Object> factory =
//                new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
//
//        // Явно устанавливаем префикс транзакций из YAML
//        String txIdPrefix = properties.getProducer().getTransactionIdPrefix();
//        if (txIdPrefix != null) {
//            factory.setTransactionIdPrefix(txIdPrefix);
//        }
//        return factory;
//    }
//
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
//        KafkaTemplate<String, Object> template = new KafkaTemplate<>(pf);
//        // ЭТОТ МЕТОД ОБЯЗАТЕЛЕН
//        template.setObservationEnabled(true);
//        return template;
//    }
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//            ConsumerFactory<String, Object> consumerFactory) {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//
//        // ЭТО ВКЛЮЧАЕТ ЧТЕНИЕ ТВОИХ X-B3-TraceId ИЗ ЗАГОЛОВКОВ
//        factory.getContainerProperties().setObservationEnabled(true);
//
//        return factory;
//    }
//}
