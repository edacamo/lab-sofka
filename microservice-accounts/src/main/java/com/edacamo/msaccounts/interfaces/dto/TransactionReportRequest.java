package com.edacamo.msaccounts.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Petición para obtener reporte por fechas y por usuario")
public class TransactionReportRequest {

    @Schema(description = "Nombre de usuario", example = "jlema")
    private String clientId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Schema(description = "Fecha y hora de inicio para el reporte (formato: yyyy-MM-dd'T'HH:mm:ss.SSS)",
            example = "2025-05-15T17:00:00.524"
    )
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Schema(
            description = "Fecha y hora final para el reporte (formato: yyyy-MM-dd'T'HH:mm:ss.SSS)",
            example = "2025-05-15T23:59:59.524"
    )
    private LocalDateTime endDate;
}
