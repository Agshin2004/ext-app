package com.agshin.extapp.model.request.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @Size(max = 32) String username,
        @Size(max = 80) @NotNull String email,
        @Size(max = 80) String password
) {
}
