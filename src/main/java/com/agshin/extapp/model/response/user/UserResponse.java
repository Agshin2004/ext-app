package com.agshin.extapp.model.response.user;

import com.agshin.extapp.model.enums.RegistrationStatus;
import com.agshin.extapp.model.enums.Role;

public record UserResponse(String username, String email, Role role, RegistrationStatus registrationStatus, String jwt) {
}
