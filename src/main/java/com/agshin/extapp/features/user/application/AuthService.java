package com.agshin.extapp.features.user.application;

import com.agshin.extapp.features.user.domain.User;
import com.agshin.extapp.shared.security.CustomUserDetails;

public interface AuthService {
    CustomUserDetails getCurrentPrincipal();

    User getCurrentUser();

    Long getCurrentUserId();
}
