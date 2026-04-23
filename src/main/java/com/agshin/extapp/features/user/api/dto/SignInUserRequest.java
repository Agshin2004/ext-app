package com.agshin.extapp.features.user.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;

public record SignInUserRequest(@Email String email, String password) {
}
