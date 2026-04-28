package com.agshin.extapp.features.analytics.api.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record MonthlyInsideDto(
        BigDecimal amount,
        Long totalExpenses,
        Double averageExpenseAmount,
        BigDecimal largestExpense,
        Long categoriesUsed,
        List<CategoryTotalDto> totalSpentByCategory,
        List<Map<String, Double>> percentageShare) {
}
