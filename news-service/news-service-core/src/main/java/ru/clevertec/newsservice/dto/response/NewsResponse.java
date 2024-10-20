package ru.clevertec.newsservice.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @param id    идентификатор новости
 * @param time  Время создания новости (генерируется автоматически).
 * @param title Заголовок новости.
 * @param text  Текст новости.
 * @author Katerina
 * @version 1.0.0
 * Сущность для передачи данных о новости пользователю.
 */
@Builder
public record NewsResponse(
        Long id,
        LocalDateTime time,
        String username,
        String title,
        String text
) implements Serializable {
}
