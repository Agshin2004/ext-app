package com.agshin.extapp.advice;

import com.agshin.extapp.exceptions.DataExistsException;
import com.agshin.extapp.exceptions.UnauthorizedException;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.response.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonAdvice {
    private static final Logger log = LoggerFactory.getLogger(CommonAdvice.class);


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        var response = GenericResponse.create(ApplicationConstants.SUCCESS, ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(DataExistsException.class)
    public ResponseEntity<GenericResponse<String>> handleDataExistsException(DataExistsException ex) {
        var response = GenericResponse.create(ApplicationConstants.SUCCESS, ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericResponse<String>> handleException(RuntimeException ex) {
        var response = GenericResponse.create(ApplicationConstants.ERROR, "server error", HttpStatus.INTERNAL_SERVER_ERROR.value());

        log.info(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleException(Exception ex) {
        var response = GenericResponse.create(ApplicationConstants.ERROR, "server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
