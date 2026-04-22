package com.agshin.extapp.services;

import com.agshin.extapp.config.CustomUserDetails;
import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.UserRepository;
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
