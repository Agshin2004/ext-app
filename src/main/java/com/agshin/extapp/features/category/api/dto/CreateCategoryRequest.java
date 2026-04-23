package com.agshin.extapp.features.category.api.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(@NotNull String categoryName) {
}
