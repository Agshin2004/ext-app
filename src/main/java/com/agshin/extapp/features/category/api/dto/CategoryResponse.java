package com.agshin.extapp.features.category.api.dto;

import com.agshin.extapp.features.category.api.dto.CategoryDto;

import java.util.List;

public record CategoryResponse(List<CategoryDto> categories, int currentPage, long totalElements, int totalPages) {
}
