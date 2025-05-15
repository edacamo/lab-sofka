package com.edacamo.msaccounts.infrastructure.controllers;

import com.edacamo.msaccounts.domain.entities.Movements;
import com.edacamo.msaccounts.infrastructure.exception.ResponseCode;
import com.edacamo.msaccounts.interfaces.dto.MovementRequest;
import com.edacamo.msaccounts.interfaces.dto.ResponseGeneric;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportRequest;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportResponse;
import com.edacamo.msaccounts.services.MovementsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("movimientos")
@Tag(name = "Movimientos", description = "Operaciones relacionadas con movimientos de cuentas")
public class MovementsController {

    final  private MovementsService movementsService;

    @Operation(summary = "Listar movimientos por cuenta", description = "Obtiene la lista de movimientos asociados a una cuenta específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = ResponseGeneric.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{cuentaId}")
    public ResponseEntity<ResponseGeneric<List<Movements>>> list(@PathVariable Long cuentaId) {
        List<Movements> movements = this.movementsService.getMovimientosByCuenta(cuentaId);

        ResponseGeneric<List<Movements>> response = ResponseGeneric.success(
                HttpStatus.OK.value(),
                ResponseCode.SUCCESS,
                movements
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Registrar un nuevo movimiento", description = "Registra un nuevo movimiento asociado a una cuenta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = ResponseGeneric.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error al registrar el movimiento", content = @Content)
    })
    @PostMapping("/registrar")
    public ResponseEntity<ResponseGeneric<Movements>> createAccount(@RequestBody MovementRequest request) {
        if(request != null) {
            try{
                Movements register = this.movementsService.createMovimiento(request);

                ResponseGeneric<Movements> response = ResponseGeneric.success(
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

    @Operation(summary = "Eliminar un movimiento", description = "Elimina un movimiento existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento eliminado exitosamente",
                    content = @Content(schema = @Schema(implementation = ResponseGeneric.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error al eliminar el movimiento", content = @Content)
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ResponseGeneric<String>> deleteAccount(@PathVariable Long id) {
        try{
            this.movementsService.deleteMovimiento(id);
            String message =  String.format("El movimiento id %s fue eliminado exitosamente.", id);
            ResponseGeneric<String> response = ResponseGeneric.success(
                    HttpStatus.OK.value(),
                    ResponseCode.DATA_DELETE,
                    message
            );

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Error al eliminar movimiento: ", ex);
            throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al eliminar movimiento" : ex.getMessage());
        }
    }

    @Operation(summary = "Obtener reporte de transacciones", description = "Genera un reporte de movimientos basado en criterios como fechas y usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado exitosamente",
                    content = @Content(schema = @Schema(implementation = ResponseGeneric.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos para el reporte", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error al generar el reporte", content = @Content)
    })
    @PostMapping("/reporte")
    public ResponseEntity<ResponseGeneric<List<TransactionReportResponse>>> getMovementsReport(@RequestBody TransactionReportRequest request) {
        if(request != null) {
            try{
                List<TransactionReportResponse> register = this.movementsService.getMovementsReport(request);

                ResponseGeneric<List<TransactionReportResponse>> response = ResponseGeneric.success(
                        HttpStatus.OK.value(),
                        ResponseCode.SUCCESS,
                        register
                );

                return ResponseEntity.ok(response);

            } catch (Exception ex) {
                log.error("Error al obtener informacion para el reporte: ", ex);
                throw new RuntimeException(ex.getMessage().isEmpty() ? "Error al obtener información del reporte" : ex.getMessage());
            }
        } else {
            log.error("Parametros para reporte no puede ser nula");
            throw new IllegalArgumentException("Los parametros de reporte no puede ser nulo.");
        }
    }
}
