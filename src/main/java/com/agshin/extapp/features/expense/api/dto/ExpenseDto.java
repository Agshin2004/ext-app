package com.agshin.extapp.features.expense.api.dto;

import com.agshin.extapp.features.expense.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseDto(Long userId, Long categoryId, Long recurringExpenseId, BigDecimal amount,
                         Currency currency, LocalDateTime expenseDate, String description, Boolean isRecurring) {
}
