package ru.clevertec.newsservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @param time  Время создания новости (генерируется автоматически).
 * @param title Заголовок новости.
 * @param text  Текст новости.
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для получения данных новости от пользователя.
 */
@Builder
public record NewsRequest(
        LocalDateTime time,
        @NotBlank
        @Length(min = 1, max = 255)
        String title,
        @NotBlank
        String text
) {
}
