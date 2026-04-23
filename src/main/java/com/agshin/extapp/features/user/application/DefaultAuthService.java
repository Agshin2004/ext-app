package com.agshin.extapp.features.user.application;

import com.agshin.extapp.features.user.domain.User;
import com.agshin.extapp.shared.security.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthService implements AuthService {
    @Override
    public CustomUserDetails getCurrentPrincipal() {
        Authentication authentication = getAuthentication();

        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("Not authenticated user");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails customUserDetails)) {
            throw new IllegalStateException("Unexpected authentication principal: " + principal);
        }

        return customUserDetails;
    }

    @Override
    public User getCurrentUser() {
        return getCurrentPrincipal().getUser();
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentPrincipal().getId();
    }


    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("no authenticated user");
        }

        return authentication;
    }
}
