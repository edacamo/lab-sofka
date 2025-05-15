package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.entities.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    boolean existsByNumeroCuenta(String accountNumber);
    Optional<Account> findByNumeroCuenta(String accountNumber);
}
