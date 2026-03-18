package com.agshin.extapp.controllers;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.model.constants.ApplicationConstants;
import com.agshin.extapp.model.dto.analytics.MonthlyInsideDto;
import com.agshin.extapp.model.dto.expense.ExpenseDto;
import com.agshin.extapp.model.dto.expense.PagedResponse;
import com.agshin.extapp.model.entities.Expense;
import com.agshin.extapp.model.response.GenericResponse;
import com.agshin.extapp.services.AnalyticsService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

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

        var monthlyInside = analyticsService.getMonthlyInside(
                date
        );

        var response = GenericResponse.create(ApplicationConstants.SUCCESS, monthlyInside, HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(response);
    }
}
