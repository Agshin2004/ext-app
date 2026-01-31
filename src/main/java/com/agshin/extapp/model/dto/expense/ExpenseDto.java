package com.agshin.extapp.model.dto.expense;

import com.agshin.extapp.model.entities.Expense;
import com.agshin.extapp.model.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseDto(Long userId, Long categoryId, Long recurringExpenseId, BigDecimal amount,
                         Currency currency, LocalDateTime expenseDate, String description) {
}
