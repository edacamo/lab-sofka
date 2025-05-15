package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.application.events.ClientDeletedEvent;
import com.edacamo.mspersons.application.events.ClientEvent;
import com.edacamo.mspersons.domain.entities.Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PublishClientEvent {

    private final KafkaTemplate<String, ClientEvent> kafkaTemplate;
    private final KafkaTemplate<String, ClientDeletedEvent> clientDeletedEventKafkaTemplate;

    // Método para publicar el evento al crear el cliente
    public void publishClientCreated(Client client) {
        ClientEvent event = new ClientEvent(
                client.getClienteId(),
                client.getNombre(),
                client.getGenero(),
                client.getEdad(),
                client.getIdentificacion(),
                client.getDireccion(),
                client.getTelefono(),
                client.getEstado()
        );

        log.info("Publicando evento de cliente creado: {}", event);
        kafkaTemplate.send("client-created", event.getClienteId(), event);
    }

    // Método para publicar el evento al eliminar el cliente
    public void publishClientDeleted(String clienteId) {
        ClientDeletedEvent event = new ClientDeletedEvent(clienteId);
        log.info("Publicando evento de cliente eliminado: {}", event);
        clientDeletedEventKafkaTemplate.send("client-deleted", clienteId, event);
    }
}
