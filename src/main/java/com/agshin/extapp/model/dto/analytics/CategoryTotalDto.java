package com.agshin.extapp.model.dto.analytics;

import java.math.BigDecimal;

public record CategoryTotalDto(String categoryName, BigDecimal totalAmount) {
}
