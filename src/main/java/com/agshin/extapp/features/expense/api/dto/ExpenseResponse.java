package com.agshin.extapp.features.expense.api.dto;

import com.agshin.extapp.features.expense.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(Long categoryId, Long userId, Long recurringExpenseId, BigDecimal amount, Currency currency, LocalDateTime expenseDate,
                              String description) {
}
