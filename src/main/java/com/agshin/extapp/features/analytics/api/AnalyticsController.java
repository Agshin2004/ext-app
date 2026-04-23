package com.agshin.extapp.features.analytics.api;

import com.agshin.extapp.features.analytics.api.dto.MonthlyInsideDto;
import com.agshin.extapp.features.analytics.application.AnalyticsService;
import com.agshin.extapp.shared.api.ApplicationConstants;
import com.agshin.extapp.shared.api.GenericResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<GenericResponse<MonthlyInsideDto>> getMonthlyInside(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {

        var monthlyInside = analyticsService.getMonthlyInside(date);
        var response = GenericResponse.create(ApplicationConstants.SUCCESS, monthlyInside, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }
}
