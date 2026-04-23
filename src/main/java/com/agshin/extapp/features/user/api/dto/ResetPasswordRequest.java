package com.agshin.extapp.features.user.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(String rawToken, @Size(min = 8, max = 80) String newPassword) {
}
