package com.agshin.extapp.model.dto.analytics;

import java.math.BigDecimal;

public record MonthlyInsideDto(
        BigDecimal amount,
        Integer totalExpenses,
        Integer averageExpenseAmount,
        Integer largestExpense) {
}
