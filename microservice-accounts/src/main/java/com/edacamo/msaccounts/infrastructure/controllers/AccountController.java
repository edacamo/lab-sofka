package com.edacamo.msaccounts.infrastructure.controllers;

import com.edacamo.msaccounts.domain.entities.Account;
import com.edacamo.msaccounts.infrastructure.exception.ResponseCode;
import com.edacamo.msaccounts.interfaces.dto.AccountRegisterRequest;
import com.edacamo.msaccounts.interfaces.dto.ResponseGeneric;
import com.edacamo.msaccounts.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ResponseGeneric<List<Account>>> list() {
        List<Account> clients = this.accountService.getAllCuentas();

        ResponseGeneric<List<Account>> response = ResponseGeneric.success(
                HttpStatus.OK.value(),
                ResponseCode.SUCCESS,
                clients
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    public ResponseEntity<ResponseGeneric<Account>> createAccount(@RequestBody AccountRegisterRequest account) {
        if(account != null) {
            try{
                Account register = this.accountService.createCuenta(account);
                ResponseGeneric<Account> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_CREATED,
                        register
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                // En caso de error, lanzamos una excepción personalizada para que sea capturada por el GlobalExceptionHandler
                log.error("Error al registrar la cuenta: ", ex);
                throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al registrar la cuenta" : ex.getMessage());
            }
        } else {
            // Si la cuenta es nulo, lanzamos una excepción de validación
            log.error("Cuenta nula no puede ser nula");
            throw new IllegalArgumentException("Los datos de la cuenta no puede ser nulo");
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ResponseGeneric<Account>> updateAccount(@RequestBody AccountRegisterRequest account, @PathVariable Long id) {
        if(account != null) {
            try{
                Account register = this.accountService.updateCuenta(id, account);
                ResponseGeneric<Account> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.DATA_UPDATED,
                        register
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                log.error("Error al actualizar la cuenta: ", ex);
                throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al actualizar la cuenta" : ex.getMessage());
            }
        } else {
            // Si la cuenta es nulo, lanzamos una excepción de validación
            log.error("Datos de la cuenta no puede ser nula");
            throw new IllegalArgumentException("Los datos de la cuenta no puede ser nulo");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseGeneric<String>> deleteAccount(@PathVariable Long id) {
        try{
            this.accountService.deleteCuenta(id);
            String message =  String.format("Cuenta con id %s eliminada exitosamente", id);
            ResponseGeneric<String> response = ResponseGeneric.success(
                    HttpStatus.OK.value(),
                    ResponseCode.DATA_DELETE,
                    message
            );

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error al eliminar la cuenta: ", ex);
            throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al eliminar la cuenta" : ex.getMessage());
        }
    }
}
