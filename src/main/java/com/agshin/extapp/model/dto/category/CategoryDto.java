package com.agshin.extapp.model.dto.category;

import com.agshin.extapp.model.dto.user.UserDto;

public record CategoryDto(String categoryName, UserDto user) {
}
