package com.agshin.extapp.services;

import com.agshin.extapp.model.entities.User;
import com.agshin.extapp.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthService implements AuthService {
    private final UserRepository userRepository;

    public DefaultAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        Authentication auth = getAuthentication();

        String email = auth.getName(); // email
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException("Authentication user not found in DB")
                );
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
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
