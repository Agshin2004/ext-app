package com.agshin.extapp.model.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(String rawToken, @Size(min = 8, max = 80) String newPassword) {
}
