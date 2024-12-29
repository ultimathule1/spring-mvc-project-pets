package dev.mvc_users_pets_project.model;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailMessage,
        LocalDateTime dateTime
) {
}
