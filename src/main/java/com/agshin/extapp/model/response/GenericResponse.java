package com.agshin.extapp.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GenericResponse<P> {
    private String message;
    private P body;
    private Integer statusCode;
    private LocalDateTime timestamp;

    private GenericResponse() {
    }

    public GenericResponse(String message, P body, Integer statusCode, LocalDateTime timestamp) {
        this.message = message;
        this.body = body;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public static <T> GenericResponse<T> create(
            String message,
            T body,
            Integer statusCode
    ) {
        return new GenericResponse<>(message, body, statusCode, LocalDateTime.now());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public P getBody() {
        return body;
    }

    public void setBody(P body) {
        this.body = body;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
