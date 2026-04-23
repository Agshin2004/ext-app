package com.agshin.extapp.features.expense.api.dto;

import com.agshin.extapp.features.expense.domain.RecurringExpenseFrequency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecurringExpenseResponse(long categoryId, BigDecimal amount, String description,
                                       RecurringExpenseFrequency frequency,
                                       LocalDateTime startDate, LocalDateTime endDate) {
}
