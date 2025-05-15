package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.application.events.ClientEvent;
import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.infrastructure.exception.ResponseCode;
import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;
import com.edacamo.mspersons.interfaces.dto.ResponseGeneric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    final private ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final PublishClientCreatedEvent publishClientCreatedEvent;

    public RegistrationServiceImpl(ClientRepository clientRepository,
                                   PasswordEncoder passwordEncoder,
                                   KafkaTemplate<String, ClientEvent> kafkaTemplate,
                                   PublishClientCreatedEvent publishClientCreatedEvent) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.publishClientCreatedEvent = publishClientCreatedEvent;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {

        if (clientRepository.findByClienteId(request.getUsuario()) != null) {
            return new RegisterResponse(String.format("El usuario %s ya existe.", request.getUsuario()));
        }

        Client cliente = new Client();
        cliente.setClienteId(request.getUsuario());
        cliente.setContrasenia(passwordEncoder.encode(request.getPassword()));
        cliente.setEstado(true);

        cliente.setNombre(request.getNombre());
        cliente.setGenero(request.getGenero());
        cliente.setEdad(request.getEdad());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setDireccion(request.getDireccion());
        cliente.setTelefono(request.getTelefono());

        clientRepository.save(cliente);
        this.publishClientCreatedEvent.publishClientCreated(cliente);//Produce el mensaje Kafka
        return new RegisterResponse("Usuario registrado correctamente");
    }

    @Override
    public RegisterResponse updateUser(RegisterRequest request) {

        Client clientDB = this.clientRepository.findByClienteId(request.getUsuario());

        if (clientDB != null) {
            clientDB.setNombre(request.getNombre());
            clientDB.setIdentificacion(request.getIdentificacion());
            clientDB.setEdad(request.getEdad());
            clientDB.setGenero(request.getGenero());
            clientDB.setDireccion(request.getDireccion());
            clientDB.setTelefono(request.getTelefono());
            clientDB.setEstado(request.getEstado());

            clientRepository.save(clientDB);
            return new RegisterResponse("La información del usuario fue actualizada correctamente.");
        } else {
            return new RegisterResponse("La información del usuario no existe.");
        }
    }

    @Override
    @Transactional
    public RegisterResponse deleteUser(String clienteId) {
        Client client = clientRepository.findByClienteId(clienteId);
        if (client == null) {
            return new RegisterResponse("El cliente no existe.");
        }

        clientRepository.delete(client); // Esto elimina cliente + persona (por herencia JOINED)
        this.publishClientCreatedEvent.publishClientDeleted(clienteId);
        return new RegisterResponse("Cliente eliminado correctamente.");
    }
}