package com.agshin.extapp.model.request.expense;

import com.agshin.extapp.model.enums.RecurringExpenseFrequency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecurringExpenseResponse(long categoryId, BigDecimal amount, String description,
                                       RecurringExpenseFrequency frequency,
                                       LocalDateTime startDate, LocalDateTime endDate) {
}
