package com.agshin.extapp.controllers;

import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    @GetMapping("/monthly-summary")
    public ResponseEntity<GenericResponse<?>> getMonthlyInside(@RequestParam("month") String date) {
        GenericResponse<String> response = GenericResponse.create(ApplicationConstants.SUCCESS, date, HttpStatus.OK.value());

        return ResponseEntity.ok().body(response);
    }
}
