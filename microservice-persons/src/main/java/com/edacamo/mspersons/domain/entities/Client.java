package com.edacamo.mspersons.domain.entities;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="cliente")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Client extends Person {

    @Column(name = "cliente_id", unique = true, nullable = false)
    private String clienteId;

    @Column(unique = true, nullable = false, length = 255)
    private String contrasenia;

    @Column(nullable = false)
    private Boolean estado;
}