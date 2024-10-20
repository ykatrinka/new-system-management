package ru.clevertec.newsservice.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Новость с комментариями.
 *
 * @param id       идентификатор новости
 * @param time     Время создания новости (генерируется автоматически).
 * @param title    Заголовок новости.
 * @param text     Текст новости.
 * @param comments Список комментариев к новости.
 * @author Katerina
 * @version 1.0.0
 */
@Builder
public record NewsCommentsResponse(
        Long id,
        LocalDateTime time,
        String username,
        String title,
        String text,
        List<CommentResponse> comments
) implements Serializable {
}
