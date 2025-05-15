package com.edacamo.msaccounts.services;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.domain.entities.Movements;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import com.edacamo.msaccounts.domain.repositories.MovementsRepository;
import com.edacamo.msaccounts.interfaces.dto.AccountRegisterRequest;
import com.edacamo.msaccounts.interfaces.dto.MovementRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountMovementVerificationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MovementsService movementsService;

    @Autowired
    private MovementsRepository movementsRepository;

    @Test
    void shouldCreateAccountAndAddMovements() {
        // Arrange: Datos de prueba
        AccountRegisterRequest accountRequest = new AccountRegisterRequest();
        accountRequest.setNumeroCuenta("123456789");
        accountRequest.setTipo("AHORROS");
        accountRequest.setSaldoInicial(new BigDecimal("1000"));
        accountRequest.setClienteId("epenaranda");
        accountRequest.setEstado(true);

        // Act: Crear la cuenta
        Account createdAccount = accountService.createCuenta(accountRequest);

        // Verificar que la cuenta se creó correctamente
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getId());
        assertEquals("123456789", createdAccount.getNumeroCuenta());
        assertEquals("AHORROS", createdAccount.getTipo());

        // Act: Agregar un movimiento de tipo 'C' (Crédito)
        MovementRequest movementRequest = new MovementRequest(null, "C", new BigDecimal("200"), "123456789");
        movementsService.createMovimiento(movementRequest);

        // Act: Agregar un movimiento de tipo 'D' (Débito)
         movementRequest = new MovementRequest(null, "D", new BigDecimal("100"), "123456789");
        movementsService.createMovimiento(movementRequest);

        // Assert: Verificar que los movimientos fueron agregados a la cuenta
        List<Movements> movements = movementsRepository.findByCuentaId(createdAccount.getId());
        assertEquals(2, movements.size());

        Movements creditMovement = movements.get(0);
        assertEquals(new BigDecimal("200"), creditMovement.getValor());
        assertEquals("C", creditMovement.getTipoMovimiento());

        Movements debitMovement = movements.get(1);
        assertEquals(new BigDecimal("100"), debitMovement.getValor());
        assertEquals("D", debitMovement.getTipoMovimiento());

        // Assert: Verificar que la cuenta tiene los movimientos esperados
        assertEquals(new BigDecimal("1100"), createdAccount.getSaldoInicial());  // 1000 (saldo inicial) + 200 (crédito) - 100 (débito)
    }
}
