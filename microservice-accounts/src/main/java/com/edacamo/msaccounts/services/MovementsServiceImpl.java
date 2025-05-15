package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.domain.entities.Movements;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import com.edacamo.msaccounts.domain.repositories.MovementsRepository;
import com.edacamo.msaccounts.infrastructure.exception.InsufficientFundsException;
import com.edacamo.msaccounts.interfaces.dto.MovementRequest;
import com.edacamo.msaccounts.interfaces.dto.ResponseGeneric;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportRequest;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MovementsServiceImpl implements MovementsService {

    public static final String DEPOSIT = "C";
    public static final String WITHDRAWAL = "D";

    final private AccountRepository accountRepository;
    final private MovementsRepository movementsRepository;

    @Override
    @Transactional
    public Movements createMovimiento(MovementRequest movimientoRequest) {
        Account account = accountRepository.findByNumeroCuenta(movimientoRequest.getNumeroCuenta())
                .orElseThrow(() -> new EmptyResultDataAccessException("Cuenta no encontrada", 1));

        if (!Boolean.TRUE.equals(account.getEstado())) {
            throw new RuntimeException("No se pueden realizar movimientos en una cuenta inactiva.");
        }

        if (!movimientoRequest.getTipoMovimiento().equalsIgnoreCase(DEPOSIT) &&
                !movimientoRequest.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            throw new RuntimeException("Tipo de movimiento no válido. Debe ser D (Debito) o C (Credito).");
        }

        BigDecimal saldoInicial = account.getSaldoInicial();

        //Obtiene saldo (Retiro o Deposito)
        BigDecimal balance = getBalance(movimientoRequest, account);

        account.setSaldoInicial(balance);
        accountRepository.save(account);

        String description;

        if (movimientoRequest.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            description = "Retiro de " + movimientoRequest.getValor();
        } else {
            description = "Deposito de " + movimientoRequest.getValor();
        }

        Movements movement = new Movements();
        movement.setTipoMovimiento(movimientoRequest.getTipoMovimiento());
        movement.setDescripcion(description);
        movement.setSaldoInicial(saldoInicial);
        movement.setValor(movimientoRequest.getValor());
        movement.setSaldo(balance);

        movement.setFecha(movimientoRequest.getFecha() != null ? movimientoRequest.getFecha() : LocalDateTime.now());
        movement.setAccount(account);

        return this.movementsRepository.save(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movements> getMovimientosByCuenta(Long cuentaId) {
        return this.movementsRepository.findByCuentaId(cuentaId);
    }

    @Override
    public void deleteMovimiento(Long movimientoId) {

        Movements movimiento = this.movementsRepository.findById(movimientoId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Movimiento no encontrado", 1));

        Account account = movimiento.getAccount();

        List<Movements> movimientos = this.movementsRepository.findByCuentaId(account.getId());

        if (movimientos.isEmpty()) {
            throw new EmptyResultDataAccessException("No hay movimientos registrados para esta cuenta.", 1);
        }

        Movements ultimoMovimiento = movimientos.get(movimientos.size() - 1);

        if (!ultimoMovimiento.getId().equals(movimientoId)) {
            throw new RuntimeException("Solo se puede eliminar el último movimiento registrado");
        }

        account.setSaldoInicial(account.getSaldoInicial().subtract(movimiento.getValor()));
        this.accountRepository.save(account);

        this.movementsRepository.delete(movimiento);
    }

    @Override
    public Movements updateMovimiento(Long id, Movements movimientoDetails) {

        Movements movimiento = this.movementsRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Movimiento no encontrado", 1));

        movimiento.setFecha(movimientoDetails.getFecha());
        movimiento.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
        movimiento.setValor(movimientoDetails.getValor());
        movimiento.setSaldo(movimientoDetails.getSaldo());
        movimiento.setAccount(movimientoDetails.getAccount());

        this.movementsRepository.save(movimiento);

        return movimiento;
    }

    @Override
    public List<TransactionReportResponse> getMovementsReport(TransactionReportRequest request) {

        List<Movements> movs = this.movementsRepository.findByAccountAndDateRange(
                request.getClientId(),
                request.getStartDate(),
                request.getEndDate());

        List<TransactionReportResponse> responses = new ArrayList<>();

        if (!movs.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (Movements mov : movs) {
                TransactionReportResponse r = new TransactionReportResponse(
                        mov.getFecha().format(formatter),
                        mov.getAccount().getClient().getNombre(),
                        mov.getAccount().getNumeroCuenta(),
                        mov.getAccount().getTipo(),
                        mov.getSaldoInicial(),
                        mov.getAccount().getEstado(),
                        mov.getValor(),
                        mov.getSaldo()
                );
                responses.add(r);
            }
        }

        return responses;
    }

    private static BigDecimal getBalance(MovementRequest movementRequest, Account account) {
        BigDecimal balance;

        if (movementRequest.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            balance = account.getSaldoInicial().subtract(movementRequest.getValor().abs());
        } else {
            balance = account.getSaldoInicial().add(movementRequest.getValor());
        }

        if (movementRequest.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL) && balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar el retiro");
        }
        return balance;
    }
}
