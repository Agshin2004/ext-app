package com.agshin.extapp.advice;

import com.agshin.extapp.exceptions.*;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.response.GenericResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class CommonAdvice {
    private static final Logger log = LoggerFactory.getLogger(CommonAdvice.class);

    private final Environment env;

    public CommonAdvice(Environment env) {
        this.env = env;
    }

    private boolean isDev() {
        return Arrays.asList(env.getActiveProfiles()).contains("dev");
    }


    private String getMessage(Exception ex) {
        return isDev() ? ex.getMessage() : "Server error";
    }

    private GenericResponse<String> buildResponse(Exception ex, HttpStatus status) {
        String msg = ApplicationConstants.ERROR;
        return GenericResponse.create(msg, getMessage(ex), status.value());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        var response = buildResponse(ex, HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<GenericResponse<String>> handleValidationException(ValidationException ex) {
        var response = buildResponse(ex, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(DataExistsException.class)
    public ResponseEntity<GenericResponse<String>> handleDataExistsException(DataExistsException ex) {
        var response = buildResponse(ex, HttpStatus.UNPROCESSABLE_ENTITY);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleDataNotFoundException(DataNotFoundException ex) {
        var response = buildResponse(ex, HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var response = buildResponse(ex, HttpStatus.CONFLICT);

        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<GenericResponse<String>> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        var response = buildResponse(ex, HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(response);
    }

    @ExceptionHandler
    public ResponseEntity<GenericResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        var response = buildResponse(ex, HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(response);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<GenericResponse<String>> handleInvalidFormatException(InvalidFormatException ex) {
        var response = buildResponse(ex, HttpStatus.UNAUTHORIZED);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GenericResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
