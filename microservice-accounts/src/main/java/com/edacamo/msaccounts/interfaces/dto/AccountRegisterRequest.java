package com.edacamo.msaccounts.interfaces.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRegisterRequest {
    private String numeroCuenta;
    private String tipo;
    private BigDecimal saldoInicial;
    private Boolean estado;
    private String clienteId;
}
