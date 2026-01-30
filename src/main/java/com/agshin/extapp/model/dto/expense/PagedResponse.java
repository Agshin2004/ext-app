package com.agshin.extapp.model.dto.expense;

import com.agshin.extapp.model.dto.category.CategoryDto;

import java.util.List;

public record PagedResponse<T>(T data,
                               int currentPage,
                               long totalElements,
                               int totalPages) {
}
