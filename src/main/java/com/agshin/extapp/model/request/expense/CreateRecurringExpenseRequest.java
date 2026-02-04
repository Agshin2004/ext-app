package com.agshin.extapp.model.request.expense;

import com.agshin.extapp.model.enums.RecurringExpenseFrequency;
import com.agshin.extapp.validators.ValidRecurringDates;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ValidRecurringDates
public record CreateRecurringExpenseRequest(
        @Nullable Long categoryId,
        @Nullable BigDecimal amount,
        @NotNull String description,
        @NotNull RecurringExpenseFrequency frequency,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate) {
}
