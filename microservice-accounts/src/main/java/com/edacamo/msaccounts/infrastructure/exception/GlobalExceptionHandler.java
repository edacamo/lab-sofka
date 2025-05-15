package com.edacamo.msaccounts.infrastructure.exception;

import com.edacamo.msaccounts.infrastructure.utils.Helpers;
import com.edacamo.msaccounts.interfaces.dto.ResponseGeneric;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ResponseGeneric<Object>> handleBadRequest(Exception ex) {
        ResponseGeneric<Object> response = buildErrorResponse(ex, HttpStatus.BAD_REQUEST, ResponseCode.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseGeneric<Object>> handleGenericException(Exception ex) {
        ResponseGeneric<Object> response = buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseGeneric<Object>> handleConflictException(ConflictException ex) {
        log.error("Error de conflicto: {}", ex.getMessage());

        ResponseGeneric<Object> response = ResponseGeneric.error(
                HttpStatus.CONFLICT.value(),
                ResponseCode.CONFLICT_ERROR,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseGeneric<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ResponseGeneric<Map<String, String>> response = ResponseGeneric.error(
                HttpStatus.BAD_REQUEST.value(),
                ResponseCode.VALIDATION_ERROR,
                errors.toString()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        ResponseGeneric<Object> response = buildErrorResponse(e, HttpStatus.NOT_FOUND, ResponseCode.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<?> handleBadSqlGrammarException(BadSqlGrammarException e) {
        ResponseGeneric<Object> response = buildErrorResponse(e, HttpStatus.BAD_REQUEST, ResponseCode.SQL_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<?> handleQueryTimeoutException(QueryTimeoutException e) {
        ResponseGeneric<Object> response = buildErrorResponse(e, HttpStatus.BAD_REQUEST, ResponseCode.TIMEOUT_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccessException(DataAccessException e) {
        ResponseGeneric<Object> response = buildErrorResponse(e, HttpStatus.BAD_REQUEST, ResponseCode.DATA_ACCESS_ERROR);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Método para obtener el mensaje de la causa de manera recursiva
    private String obtenerMensajeCausa(Throwable throwable) {
        Throwable causa = throwable.getCause();

        if (causa != null) {
            return obtenerMensajeCausa(causa);
        } else {
            log.error("Error encontrado: ", throwable);
            // Cuando no hay más causas, retornamos el mensaje de la excepción actual
            return throwable.getMessage();
        }
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ResponseGeneric<Object>> handleInsufficientFundsException(InsufficientFundsException ex) {
        log.error("Error de saldo insuficiente: {}", ex.getMessage());

        ResponseGeneric<Object> response = ResponseGeneric.error(
                HttpStatus.BAD_REQUEST.value(),
                ResponseCode.INSUFFICIENT_FUNDS,
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseGeneric<Object> buildErrorResponse(Exception ex, HttpStatus status, ResponseCode responseCode) {
        String mensajeCausa = obtenerMensajeCausa(ex);
        String mensajeLimpio = mensajeCausa.isEmpty() ? "" : Helpers.cleanErrorMessage(mensajeCausa);
        return ResponseGeneric.error(status.value(), responseCode, mensajeLimpio);
    }
}
