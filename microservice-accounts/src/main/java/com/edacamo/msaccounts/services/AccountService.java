package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.interfaces.dto.AccountRegisterRequest;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    public Account createCuenta(AccountRegisterRequest cuenta);

    public List<Account> getAllCuentas();

    Optional<Account> getCuentaById(Long id);

    public Account updateCuenta(Long id, AccountRegisterRequest cuentaDetails);

    public void deleteCuenta(Long id);
}
