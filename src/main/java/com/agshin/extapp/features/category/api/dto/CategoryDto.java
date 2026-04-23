package com.agshin.extapp.features.category.api.dto;

import com.agshin.extapp.features.user.api.dto.UserDto;

public record CategoryDto(String categoryName, UserDto user) {
}
