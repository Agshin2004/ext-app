package com.agshin.extapp.model.response.category;

import com.agshin.extapp.model.dto.category.CategoryDto;
import com.agshin.extapp.model.entities.Category;

import java.util.List;

public record CategoryResponse(List<CategoryDto> categories, int currentPage, long totalElements, int totalPages) {
}
