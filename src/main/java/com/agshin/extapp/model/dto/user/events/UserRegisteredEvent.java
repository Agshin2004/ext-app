package com.agshin.extapp.model.dto.user.events;

import java.time.Instant;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String name,
        Instant registeredAt) {
}
