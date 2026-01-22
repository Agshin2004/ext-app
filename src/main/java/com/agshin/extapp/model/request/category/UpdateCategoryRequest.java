package com.agshin.extapp.model.request.category;

import jakarta.validation.constraints.NotNull;

public record UpdateCategoryRequest(@NotNull String categoryName) {
}
