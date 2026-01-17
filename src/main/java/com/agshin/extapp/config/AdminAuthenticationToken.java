package com.agshin.extapp.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;


public class AdminAuthenticationToken extends AbstractAuthenticationToken {

    private final CustomUserDetails principal;

    public AdminAuthenticationToken(CustomUserDetails principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null; // already authenticated
    }
}
