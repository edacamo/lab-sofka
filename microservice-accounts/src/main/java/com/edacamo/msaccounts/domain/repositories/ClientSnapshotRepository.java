package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.domain.entities.ClientSnapshot;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientSnapshotRepository extends CrudRepository<ClientSnapshot, Long> {
    Optional<ClientSnapshot> findByClienteId(String clienteId);
}
