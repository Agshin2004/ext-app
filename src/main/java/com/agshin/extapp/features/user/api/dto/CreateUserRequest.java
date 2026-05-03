package com.agshin.extapp.features.user.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(min = 2, max = 32) @NotNull String username,
        @Size(max = 80) @NotNull String email,
        @Size(min = 8, max = 80) String password
) {
}
