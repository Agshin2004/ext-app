package com.agshin.extapp.features.analytics.api.dto;

import java.math.BigDecimal;

public record CategoryTotalDto(String categoryName, BigDecimal totalAmount, Long total) {
}
