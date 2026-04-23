package com.agshin.extapp.features.expense.api.dto;

import com.agshin.extapp.features.expense.domain.Currency;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateExpenseRequest(
        @Nullable Long categoryId,
        @Nullable Long recurringExpenseId,
        @NotNull BigDecimal amount,
        @NotNull Currency currency,
        @NotNull LocalDateTime expenseDate,
        @NotNull String description) {
}
