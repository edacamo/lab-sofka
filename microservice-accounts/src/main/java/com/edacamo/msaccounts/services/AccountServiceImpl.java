package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.domain.entities.ClientSnapshot;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.exception.ConflictException;
import com.edacamo.msaccounts.interfaces.dto.AccountRegisterRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    final private AccountRepository accountRepository;
    final private ClientSnapshotRepository clientSnapshotRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ClientSnapshotRepository clientSnapshotRepository) {
        this.accountRepository = accountRepository;
        this.clientSnapshotRepository = clientSnapshotRepository;
    }

    @Override
    @Transactional()
    public Account createCuenta(AccountRegisterRequest dto) {
        if (accountRepository.existsByNumeroCuenta(dto.getNumeroCuenta())) {
            log.error("Cuenta con número {} ya existe", dto.getNumeroCuenta());
            throw new ConflictException("Cuenta con número " + dto.getNumeroCuenta() + " ya existe");
        }

        ClientSnapshot client = clientSnapshotRepository
                .findByClienteId(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Account account = new Account();
        account.setNumeroCuenta(dto.getNumeroCuenta());
        account.setTipo(dto.getTipo());
        account.setSaldoInicial(dto.getSaldoInicial());
        account.setEstado(dto.getEstado());
        account.setClient(client);

        return this.accountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllCuentas() {
        return (List<Account>) this.accountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> getCuentaById(Long id) {
        return this.accountRepository.findById(id);
    }

    @Override
    @Transactional()
    public Account updateCuenta(Long id, AccountRegisterRequest cuentaDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Cuenta no encontrada", 1));

        //Se obtiene datos del cliente snapshot
        ClientSnapshot client = clientSnapshotRepository
                .findByClienteId(cuentaDetails.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        account.setNumeroCuenta(cuentaDetails.getNumeroCuenta());
        account.setTipo(cuentaDetails.getTipo());
        account.setSaldoInicial(cuentaDetails.getSaldoInicial());
        account.setEstado(cuentaDetails.getEstado());
        account.setClient(client);

        return accountRepository.save(account);
    }

    @Override
    @Transactional()
    public void deleteCuenta(Long id) {
        // Verificar si la cuenta existe antes de intentar eliminarla
        Optional<Account> account = this.accountRepository.findById(id);
        if (account.isPresent()) {

            this.accountRepository.deleteById(id);

            // Verificar si la eliminación fue exitosa
            if (!this.accountRepository.existsById(id)) {
                log.info("Cuenta con id {} eliminada exitosamente", id);
            } else {
                log.warn("No se pudo eliminar la cuenta con id {}", id);
            }
        } else {
            log.warn("No se encontró la cuenta con id {}", id);
            throw new EntityNotFoundException("Cuenta no encontrada con id: " + id);
        }
    }
}
