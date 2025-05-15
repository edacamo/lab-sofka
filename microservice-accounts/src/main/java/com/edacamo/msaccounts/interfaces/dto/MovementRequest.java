package com.edacamo.msaccounts.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovementRequest {
    private LocalDateTime fecha;

    private String tipoMovimiento;

    private BigDecimal valor;

    private String numeroCuenta;

}
