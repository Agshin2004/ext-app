package com.agshin.extapp.advice;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.UnauthorizedException;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        var response = GenericResponse.create(ApplicationConstants.ERROR, getMessage(ex), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(DataExistsException.class)
    public ResponseEntity<GenericResponse<String>> handleDataExistsException(DataExistsException ex) {
        var response = GenericResponse.create(ApplicationConstants.ERROR, getMessage(ex), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericResponse<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        var response = GenericResponse.create(
                ApplicationConstants.ERROR,
                getMessage(ex),
                HttpStatus.CONFLICT.value()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(response
        );
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<GenericResponse<String>> handleException(RuntimeException ex) {
        log.info("Exception: {}\nException Class: {}", ex.getMessage(), ex.getClass().getName());

        var response = GenericResponse.create(ApplicationConstants.ERROR, "server error", HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.info(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
