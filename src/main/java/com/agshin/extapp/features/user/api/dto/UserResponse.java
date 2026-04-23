package com.agshin.extapp.features.user.api.dto;

import com.agshin.extapp.features.user.domain.RegistrationStatus;
import com.agshin.extapp.features.user.domain.Role;

public record UserResponse(String username, String email, Role role, RegistrationStatus registrationStatus, String jwt) {
}
