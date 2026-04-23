package com.agshin.extapp.services;

import com.agshin.extapp.mappers.ExpenseMapper;
import com.agshin.extapp.model.dto.analytics.CategoryTotalDto;
import com.agshin.extapp.model.dto.analytics.MonthlyInsideDto;
import com.agshin.extapp.repositories.CategoryRepository;
import com.agshin.extapp.repositories.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class AnalyticsService {
    private final ExpenseMapper expenseMapper;
    private final AuthService authService;
    private final CategoryRepository categoryRepository;
    Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final ExpenseRepository expenseRepository;

    public AnalyticsService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper, AuthService authService, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
        this.authService = authService;
        this.categoryRepository = categoryRepository;
    }


    /**
     * Get all monthly expenses.
     *
     * @param date
     * @return {@link MonthlyInsideDto}
     */
    public MonthlyInsideDto getMonthlyInside(YearMonth date) {
        String email = authService.getCurrentUser().getEmail();
        System.out.println(email);
        logger.info("Fetching monthly analytics");
        Long currentUserId = authService.getCurrentUserId();
        LocalDateTime start = date.atDay(1).atStartOfDay(); // add day and then time 00:00
        LocalDateTime end = date.atEndOfMonth().atTime(LocalTime.MAX); // last day of month and max time 23:59:59

        MonthlyInsideDto raw = expenseRepository.getMonthlyStats(currentUserId, start, end);
        List<CategoryTotalDto> totalSpentByCategory =
                expenseRepository.getTotalSpentByCategoryForUserAndPeriod(currentUserId, start, end);


        // removing nulls
        return new MonthlyInsideDto(
                raw.amount() != null ? raw.amount() : BigDecimal.ZERO,
                raw.totalExpenses(),
                raw.averageExpenseAmount() != null ? raw.averageExpenseAmount() : 0,
                raw.largestExpense() != null ? raw.largestExpense() : BigDecimal.ZERO,
                raw.categoriesUsed(),
                totalSpentByCategory
        );
    }
}
