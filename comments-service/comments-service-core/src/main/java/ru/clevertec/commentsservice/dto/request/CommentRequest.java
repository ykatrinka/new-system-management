package ru.clevertec.commentsservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

/**
 * @param username Имя пользователя.
 * @param newsId   Идентификатор новости.
 * @param time     Время создания комментария (генерируется автоматически).
 * @param text     Текст комментария.
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для получения данных комментария от пользователя.
 */
@Builder
public record CommentRequest(

        @NotNull
        @Length(min = 1, max = 50)
        String username,

        @NotNull
        Long newsId,

        LocalDateTime time,

        @NotBlank
        String text
) {
}




