package com.edacamo.mspersons.infrastructure.config;

import com.edacamo.mspersons.application.events.ClientDeletedEvent;
import com.edacamo.mspersons.application.events.ClientEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final String bootstrapAddress = "localhost:9092";

    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    // KafkaTemplate para ClientCreatedEvent
    @Bean
    public KafkaTemplate<String, ClientEvent> clientEventKafkaTemplate(ProducerFactory<String, ClientEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    // KafkaTemplate para ClientDeletedEvent
    @Bean
    public KafkaTemplate<String, ClientDeletedEvent> clientDeletedEventKafkaTemplate(ProducerFactory<String, ClientDeletedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
