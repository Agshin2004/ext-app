package com.agshin.extapp.model.response.expense;

import com.agshin.extapp.model.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(Long categoryId, Long userId, Long recurringExpenseId, BigDecimal amount, Currency currency, LocalDateTime expenseDate,
                              String description) {
}
