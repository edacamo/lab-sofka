package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.application.events.ClientDeletedEvent;
import com.edacamo.msaccounts.application.events.ClientEvent;
import com.edacamo.msaccounts.domain.entities.ClientSnapshot;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ClientSnapshotEventsService {

    @Autowired
    private ClientSnapshotRepository repository;

    @KafkaListener(topics = "client-created-events",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "ms-accounts-client-snapshot-created")
    public void consume(ClientEvent event) {
        try{
            // Log para ver el contenido del evento recibido
            log.info("Received ClientEvent: clienteId={}, nombre={}, edad={}, identificacion={}, direccion:{}, telefono: {}, estado={}",
                    event.getClienteId(), event.getNombre(), event.getEdad(), event.getIdentificacion(),
                    event.getDireccion(), event.getTelefono(), event.getEstado());

            // Creación de ClientSnapshot
            ClientSnapshot snapshot = new ClientSnapshot();
            snapshot.setClienteId(event.getClienteId());
            snapshot.setNombre(event.getNombre());
            snapshot.setGenero(event.getGenero());
            snapshot.setEdad(event.getEdad());
            snapshot.setIdentificacion(event.getIdentificacion());
            snapshot.setDireccion(event.getDireccion());
            snapshot.setTelefono(event.getTelefono());
            snapshot.setEstado(event.getEstado());

            // Guardar el snapshot
            repository.save(snapshot);

            // Log para confirmar que el snapshot fue guardado
            log.info("ClientSnapshot for clienteId={} saved successfully", event.getClienteId());
        } catch (Exception e){
            log.error("Error while processing ClientEvent with clienteId={}", event.getClienteId(), e);
            // Aquí puedes aplicar alguna lógica extra si deseas: alertas, reintentos, DLQ, etc.
        }
    }


    // Consumidor para ClientDeletedEvent (cuando se elimina un cliente)
    @KafkaListener(topics = "client-deleted-events",
            containerFactory = "clientDeletedEventKafkaListenerContainerFactory",
            groupId = "ms-accounts-client-snapshop-deleted")
    public void consume(ClientDeletedEvent event) {
        try {
            // Log para ver el contenido del evento recibido
            log.info("Received ClientDeletedEvent: clienteId={}", event.getClienteId());

            // Se obtiene informacion del cliente desde el snapshot
            Optional<ClientSnapshot> snapshot = repository.findByClienteId(event.getClienteId());

            if (snapshot.isPresent()) {
                repository.delete(snapshot.get());
                log.info("ClientSnapshot for clienteId={} deleted successfully", event.getClienteId());
            } else {
                log.warn("No ClientSnapshot found for clienteId={}", event.getClienteId());
            }
        } catch (Exception e) {
            log.error("Error while processing ClientDeletedEvent with clienteId={}", event.getClienteId(), e);
            // Aquí también podrías aplicar lógica extra como alertas, reintentos, DLQ, etc.
        }
    }
}
