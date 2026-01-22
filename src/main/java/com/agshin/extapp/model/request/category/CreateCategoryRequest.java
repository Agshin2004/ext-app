package com.agshin.extapp.model.request.category;

import jakarta.validation.constraints.NotNull;

public record CreateCategoryRequest(@NotNull String categoryName) {
}
