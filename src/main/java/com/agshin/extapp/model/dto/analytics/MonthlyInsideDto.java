package com.agshin.extapp.model.dto.analytics;

import java.math.BigDecimal;

public record MonthlyInsideDto(
        BigDecimal amount,
        Long totalExpenses,
        Double averageExpenseAmount,
        BigDecimal largestExpense,
        Long categoriesUsed) {
}
