package com.agshin.extapp.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;

public record SignInUserRequest(@Email String email, String password) {
}
