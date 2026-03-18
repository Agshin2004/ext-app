package com.agshin.extapp.services;

import com.agshin.extapp.mappers.ExpenseMapper;
import com.agshin.extapp.model.dto.analytics.MonthlyInsideDto;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.YearMonth;

@Service
public class AnalyticsService {
    private final ExpenseMapper expenseMapper;
    private final AuthService authService;
    Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final ExpenseRepository expenseRepository;

    public AnalyticsService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper, AuthService authService) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.authService = authService;
    }


    /**
     * Get all monthly expenses.
     *
     * @param date
     * @return
     */
    public MonthlyInsideDto getMonthlyInside(YearMonth date) {
        BigDecimal monthlyTotal = expenseRepository.getMonthlyTotal(
                authService.getCurrentUserId(),
                date.atDay(1).atStartOfDay(), // add day and then time 00:00
                date.atEndOfMonth().atTime(LocalTime.MAX) // last day of month and max time 23:59:59
        );



        return new MonthlyInsideDto(monthlyTotal);
    }
}
