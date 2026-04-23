package com.agshin.extapp.features.user.api.dto;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(@Email String email) {
}
