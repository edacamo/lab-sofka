package com.edacamo.msaccounts.infrastructure.controllers;

import com.edacamo.msaccounts.domain.entities.Movements;
import com.edacamo.msaccounts.infrastructure.exception.ResponseCode;
import com.edacamo.msaccounts.interfaces.dto.MovementRequest;
import com.edacamo.msaccounts.interfaces.dto.ResponseGeneric;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportRequest;
import com.edacamo.msaccounts.interfaces.dto.TransactionReportResponse;
import com.edacamo.msaccounts.services.MovementsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("movimientos")
public class MovementsController {

    final  private MovementsService movementsService;

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
