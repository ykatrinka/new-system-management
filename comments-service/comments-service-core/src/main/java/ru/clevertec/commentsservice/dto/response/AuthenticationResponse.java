package ru.clevertec.commentsservice.dto.response;

import lombok.Builder;

import java.io.Serializable;

/**
 * @param token Токен
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для передачи токена.
 */
@Builder
public record AuthenticationResponse(String token) implements Serializable {
}
