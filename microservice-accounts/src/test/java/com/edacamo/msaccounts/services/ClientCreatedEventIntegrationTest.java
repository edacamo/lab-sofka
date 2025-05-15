package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.application.events.ClientCreatedEvent;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import com.edacamo.msaccounts.domain.entities.ClientSnapshot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.awaitility.Awaitility;

import java.util.Optional;
import java.time.Duration;

@Log4j2
@SpringBootTest(classes = {KafkaTestConfig.class})
@EmbeddedKafka(partitions = 1, topics = "client-created", brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092", "port=9092"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientCreatedEventIntegrationTest {

    @Autowired
    private KafkaTemplate<String, ClientCreatedEvent> kafkaTemplate;

    @Autowired
    private ClientSnapshotRepository repository;

    @Test
    void shouldConsumeClientCreatedEventAndPersistClientSnapshot() {
        log.info("Simulando envío de evento 'ClientCreatedEvent' internamente y validando persistencia de ClientSnapshot");
        // Arrange
        String clientId = "jperez";
        ClientCreatedEvent event = new ClientCreatedEvent(clientId, "MockedClient", "M", 30,
                "0105896520", "Cuenca - Ecuador",
                "0985207842", true
        );

        // Act - simular publicación como si viniera de ms-persons
        kafkaTemplate.send("client-created", event);

        // Assert - usar Awaitility para esperar la persistencia
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    Optional<ClientSnapshot> result = repository.findByClienteId(clientId);
                    assertTrue(result.isPresent());
                    assertEquals("MockedClient", result.get().getNombre());
                });
    }
}
