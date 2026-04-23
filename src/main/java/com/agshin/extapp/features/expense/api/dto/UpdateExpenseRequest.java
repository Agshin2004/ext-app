package com.agshin.extapp.features.expense.api.dto;

import com.agshin.extapp.features.expense.domain.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateExpenseRequest(Long categoryId, BigDecimal amount, Currency currency, LocalDateTime expenseDate, String description) {
}
