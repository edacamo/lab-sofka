package com.edacamo.mspersons.interfaces.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Petición para registrar/actualizar un cliente")
public class RegisterRequest {
    @Schema(description = "Nombre de usuario para el inicio de sesión", example = "jlema")
    private String usuario;

    @Schema(description = "Contraseña del usuario", example = "1234")
    private String password;

    @Schema(description = "Nombre completo del cliente", example = "Jose Lema")
    private String nombre;

    @Schema(description = "Género del cliente", example = "M")
    private String genero;

    @Schema(description = "Edad del cliente", example = "30")
    private Integer edad;

    @Schema(description = "Número de identificación del cliente", example = "0102030405")
    private String identificacion;

    @Schema(description = "Dirección de residencia del cliente", example = "Otavalo sn y principal ")
    private String direccion;

    @Schema(description = "Teléfono de contacto del cliente", example = "098254785")
    private String telefono;

    @Schema(description = "Estado activo del cliente", example = "true")
    private Boolean estado;
}
