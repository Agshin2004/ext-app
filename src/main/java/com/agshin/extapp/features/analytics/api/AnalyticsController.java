package com.agshin.extapp.features.analytics.api;

import com.agshin.extapp.features.analytics.api.dto.ExpenseInsightDto;
import com.agshin.extapp.features.analytics.application.AnalyticsService;
import com.agshin.extapp.shared.api.ApplicationConstants;
import com.agshin.extapp.shared.api.GenericResponse;
import com.agshin.extapp.shared.exception.DataNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    private AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/expenses/insights")
    public ResponseEntity<GenericResponse<ExpenseInsightDto>> getMonthlyInside(
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(value = "sortAscending", defaultValue = "true")
            Boolean sortAscending) {
        var monthlyInside = analyticsService.getExpenseInsights(startDate, endDate, sortAscending);
        var response = GenericResponse.create(ApplicationConstants.SUCCESS, monthlyInside, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }
}
