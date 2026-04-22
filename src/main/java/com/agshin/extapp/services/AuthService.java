package com.agshin.extapp.services;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.model.entities.User;

public interface AuthService {
    CustomUserDetails getCurrentPrincipal();

    User getCurrentUser();

    Long getCurrentUserId();
}
