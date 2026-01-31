package com.agshin.extapp.model.request.expense;

import com.agshin.extapp.model.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateExpenseRequest(Long categoryId, BigDecimal amount, Currency currency, LocalDateTime expenseDate, String description) {
}
