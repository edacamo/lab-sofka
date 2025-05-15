package com.edacamo.msaccounts.interfaces.dto;


import com.edacamo.msaccounts.infrastructure.exception.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseGeneric<T> {


    private boolean success;
    private int statusCode;
    private String timestamp;
    private String message;
    private String error;
    private T data;
    private String errorCode;

    public ResponseGeneric(boolean success, int statusCode, String message, String error, T data, String errorCode) {
        this.success = success;
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
        this.data = data;
        this.errorCode = errorCode;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.timestamp = LocalDateTime.now().format(formatter);
    }

    public static <T> ResponseGeneric<T> success(int statusCode, ResponseCode responseCode, T data) {
        return new ResponseGeneric<>(true, statusCode, responseCode.getDescription(), null, data, responseCode.getCode());
    }

    public static <T> ResponseGeneric<T> error(int statusCode, ResponseCode responseCode, String error) {
        return new ResponseGeneric<>(false, statusCode, responseCode.getDescription(), error, null, responseCode.getCode());
    }

}
