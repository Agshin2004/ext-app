package com.agshin.extapp.features.analytics.application;

import com.agshin.extapp.features.analytics.api.dto.CategoryTotalDto;
import com.agshin.extapp.features.analytics.api.dto.MonthlyInsideDto;
import com.agshin.extapp.features.expense.infrastructure.ExpenseRepository;
import com.agshin.extapp.features.user.application.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final AuthService authService;
    Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final ExpenseRepository expenseRepository;

    public AnalyticsService(ExpenseRepository expenseRepository, AuthService authService) {
        this.expenseRepository = expenseRepository;
        this.authService = authService;
    }


    /**
     * Get all monthly expenses.
     *
     * @param date
     * @return {@link MonthlyInsideDto}
     */
    public MonthlyInsideDto getMonthlyInside(YearMonth date) {
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
                totalSpentByCategory,
                createPercentageShare(totalSpentByCategory)
        );
    }

    private List<Map<String, Double>> createPercentageShare(List<CategoryTotalDto> results) {
        BigDecimal grandTotal = results.stream()
                .map(CategoryTotalDto::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return results.stream()
                .map(dto -> {
                    Double percentage = dto.totalAmount()
                            .divide(grandTotal, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();
                    return Map.of(dto.categoryName(), percentage);
                }).toList();
    }
}
