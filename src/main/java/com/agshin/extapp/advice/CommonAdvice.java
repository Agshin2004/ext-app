package com.agshin.extapp.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class CommonAdvice {
    private static final Logger log = LoggerFactory.getLogger(CommonAdvice.class);

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex) {
        log.error("500 Error");
    }
}
