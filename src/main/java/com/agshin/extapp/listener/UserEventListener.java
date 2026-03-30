package com.agshin.extapp.listener;

import com.agshin.extapp.model.dto.user.events.UserRegisteredEvent;
import com.agshin.extapp.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public UserEventListener(ObjectMapper objectMapper, UserService userService) {
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    // ─────────────────────────────────────────────────────────
    // This method name MUST match what you passed to
    // MessageListenerAdapter("handleMessage") in the config.
    //
    // Spring calls this via reflection with:
    //   message → the JSON string payload
    //   channel → which channel it came from (e.g. "orders:events")
    //
    // The channel parameter is optional. If your method only has
    // one parameter (String message), that works fine too.
    // ─────────────────────────────────────────────────────────

    @Async
    public void handleMessage(String message, String channel) {
        try {
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);

            userService.processUserRegistered(event);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
