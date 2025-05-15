package com.edacamo.msaccounts.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="cuenta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", unique=true, nullable = false)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta")
    private String tipo;

    @Column(name = "saldo_inicial")
    private BigDecimal saldoInicial;

    @Column()
    private Boolean estado;

    // Relación con el cliente (snapshot local)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "clienteId", foreignKey = @ForeignKey(name = "fk_cuenta_cliente_snapshot"))
    private ClientSnapshot client;

}
