package com.edacamo.mspersons.application.services;

import com.edacamo.mspersons.domain.entities.Client;
import com.edacamo.mspersons.domain.repositories.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    final private ClientRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return (List<Client>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findByClienteId(String clienteId) {
        return Optional.ofNullable(repository.findByClienteId(clienteId));
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }
}
