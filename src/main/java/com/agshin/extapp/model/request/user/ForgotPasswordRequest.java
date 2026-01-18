package com.agshin.extapp.model.request.user;

import jakarta.validation.constraints.Email;

public record ForgotPasswordRequest(@Email String email) {
}
