package com.agshin.extapp.model.dto.user;

import com.agshin.extapp.model.enums.RegistrationStatus;
import com.agshin.extapp.model.enums.Role;

public record UserDto(String username, String email, Role role, RegistrationStatus registrationStatus) {
}
