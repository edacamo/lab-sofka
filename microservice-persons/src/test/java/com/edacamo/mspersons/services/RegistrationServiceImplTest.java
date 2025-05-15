package com.edacamo.mspersons.services;

import com.edacamo.mspersons.application.services.PublishClientCreatedEvent;
import com.edacamo.mspersons.application.services.RegistrationServiceImpl;
import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import com.edacamo.mspersons.interfaces.dto.RegisterRequest;
import com.edacamo.mspersons.interfaces.dto.RegisterResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PublishClientCreatedEvent publishClientCreatedEvent;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsuario("jlema");
        request.setPassword(passwordEncoder.encode("password"));
        request.setNombre("Jose lema");
        request.setGenero("M");
        request.setEdad(30);
        request.setIdentificacion("1234567890");
        request.setDireccion("Otavalo sn y principal");
        request.setTelefono("098254785");
        request.setEstado(true);

        // Mock la respuesta de clientRepository
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mockea la llamada al servicio Kafka
        doNothing().when(publishClientCreatedEvent).publishClientCreated(any(Client.class));

        // Act
        RegisterResponse response = registrationService.registerUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("Usuario registrado correctamente", response.getMessage());

        // Verifica que el método publishClientCreated haya sido llamado una vez
        verify(clientRepository, times(1)).save(any(Client.class));

        // Verifica que clientRepository.save haya sido llamado una vez
        verify(clientRepository, times(1)).save(any(Client.class));
    }
}
