package com.edacamo.msaccounts.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_snapshot")
public class ClientSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clienteId;

    private String nombre;

    private String genero;

    private int edad;

    private String identificacion;

    private String direccion;

    private String telefono;

    private Boolean estado;
}
