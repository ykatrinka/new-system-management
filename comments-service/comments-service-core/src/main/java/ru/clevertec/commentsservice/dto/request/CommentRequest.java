package ru.clevertec.commentsservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.time.LocalDateTime;

import static ru.clevertec.commentsservice.util.Constants.MAX_USERNAME_LENGTH;
import static ru.clevertec.commentsservice.util.Constants.MIN_USERNAME_LENGTH;

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
        @Length(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username,

        @NotNull
        @Schema(defaultValue = "1", description = "News id")
        Long newsId,

        LocalDateTime time,

        @NotBlank
        @Schema(defaultValue = "This is comment", description = "Text comment")
        String text
) implements Serializable {
}




