package ru.clevertec.newsservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Комментарий.
 *
 * @param id       идентификатор комментария
 * @param time     Время создания комментария (генерируется автоматически).
 * @param text     Текст комментария.
 * @param username пользователь
 * @param newsId   идентификатор новости
 * @author Katerina
 * @version 1.0.0
 */
@Builder
public record CommentResponse(
        Long id,
        LocalDateTime time,
        String text,
        String username,
        Long newsId
) {
}




