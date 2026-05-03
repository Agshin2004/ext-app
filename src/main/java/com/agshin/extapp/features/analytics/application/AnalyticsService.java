package com.agshin.extapp.features.analytics.application;

import com.agshin.extapp.features.analytics.api.dto.CategoryTotalDto;
import com.agshin.extapp.features.analytics.api.dto.ExpenseInsightDto;
import com.agshin.extapp.features.expense.infrastructure.ExpenseRepository;
import com.agshin.extapp.features.user.application.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);

    private final ExpenseRepository expenseRepository;

    public AnalyticsService(ExpenseRepository expenseRepository, AuthService authService) {
        this.expenseRepository = expenseRepository;
        this.authService = authService;
    }


    /**
     * Gets monthly expense analytics for the given month.
     *
     * @param startDate     the year and month to fetch analytics for
     * @param sortAscending whether to sort results in ascending order
     * @return the monthly analytics data
     */
    public ExpenseInsightDto getExpenseInsights(LocalDate startDate, LocalDate endDate, boolean sortAscending) {
        Long currentUserId = authService.getCurrentUserId();
        logger.info("Fetching analytics from {} to {}, for {}", startDate, endDate, currentUserId);

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX); // last day of month and max time 23:59:59

        ExpenseInsightDto raw = expenseRepository.getStatsForPeriod(
                currentUserId,
                start,
                end
        );

        List<CategoryTotalDto> totalSpentByCategory = expenseRepository.getTotalSpentByCategoryForUserAndPeriod(
                        currentUserId,
                        start,
                        end
                );



        if (sortAscending) {
            totalSpentByCategory.sort(
                    Comparator.comparing(CategoryTotalDto::totalAmount).reversed()
            );
        } else {
            totalSpentByCategory.sort(
                    Comparator.comparing(CategoryTotalDto::totalAmount)
            );
        }


        // removing nulls
        return new ExpenseInsightDto(
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
