package com.agshin.extapp.features.user.domain;

public enum Role {
    USER("USER"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public static Role fromName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
