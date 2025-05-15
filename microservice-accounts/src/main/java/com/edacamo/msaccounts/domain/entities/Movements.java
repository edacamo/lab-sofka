package com.edacamo.msaccounts.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="movimiento")
public class Movements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fecha;

    @Column(
            name="tipo_movimiento",
            nullable = false,
            length = 1
    )
    private String tipoMovimiento;

    private BigDecimal valor;

    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_movimiento_cuenta"))
    private Account account;
}
