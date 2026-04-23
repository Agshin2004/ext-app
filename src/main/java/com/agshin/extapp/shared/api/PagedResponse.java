package com.agshin.extapp.shared.api;

public record PagedResponse<T>(T data,
                               int currentPage,
                               long totalElements,
                               int totalPages) {
}
