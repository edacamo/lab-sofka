package com.edacamo.mspersons.domain.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED) // Estrategia JOINED
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(
            name = "genero",
            nullable = false,
            length = 1
    )
    private String genero;

    private int edad;

    @Column(unique = true)
    private String identificacion;

    private String direccion;

    private String telefono;
}
