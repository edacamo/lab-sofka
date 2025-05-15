package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.application.events.ClientEvent;
import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RegistrationServiceImpl implements RegistrationService {

    final private ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final PublishClientEvent publishClientEvent;

    public RegistrationServiceImpl(ClientRepository clientRepository,
                                   PasswordEncoder passwordEncoder,
                                   PublishClientEvent publishClientEvent) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.publishClientEvent = publishClientEvent;
    }

    @Transactional
    public RegisterResponse registerUser(RegisterRequest request) {

        if (clientRepository.findByClienteId(request.getUsuario()) != null) {
            return new RegisterResponse(String.format("El usuario %s ya existe.", request.getUsuario()));
        }

        Client client = new Client();
        client.setClienteId(request.getUsuario());
        client.setContrasenia(passwordEncoder.encode(request.getPassword()));
        client.setEstado(true);

        client.setNombre(request.getNombre());
        client.setGenero(request.getGenero());
        client.setEdad(request.getEdad());
        client.setIdentificacion(request.getIdentificacion());
        client.setDireccion(request.getDireccion());
        client.setTelefono(request.getTelefono());

        clientRepository.save(client);
        this.publishClientEvent.publishClientCreated(client);//Produce el mensaje Kafka
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
        this.publishClientEvent.publishClientDeleted(clienteId);
        return new RegisterResponse("Cliente eliminado correctamente.");
    }
}