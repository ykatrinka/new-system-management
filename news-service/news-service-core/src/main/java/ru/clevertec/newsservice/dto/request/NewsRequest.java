package ru.clevertec.newsservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.clevertec.newsservice.util.Constants;

import java.io.Serializable;
import java.time.LocalDateTime;

import static ru.clevertec.newsservice.util.Constants.MAX_TITLE_LENGTH;
import static ru.clevertec.newsservice.util.Constants.MIN_TITLE_LENGTH;

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
        @Length(min = MIN_TITLE_LENGTH, max = MAX_TITLE_LENGTH)
        @Schema(defaultValue = "News title", description = "Title")
        String title,

        @NotBlank
        @Schema(defaultValue = "This is content news", description = "Text news")
        String text,

        @NotNull
        @Length(min = Constants.MIN_USERNAME_LENGTH, max = Constants.MAX_USERNAME_LENGTH)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username

) implements Serializable {
}
