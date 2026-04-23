package com.agshin.extapp.features.category.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequest(@NotNull String categoryName) {
}
