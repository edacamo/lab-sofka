package com.edacamo.mspersons.domain.repositories;

import com.edacamo.mspersons.domain.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClienteId(String clienteId);
}
