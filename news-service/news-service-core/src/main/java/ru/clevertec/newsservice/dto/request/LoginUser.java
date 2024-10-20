package ru.clevertec.newsservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.clevertec.newsservice.util.Constants;

import java.io.Serializable;

/**
 * @param username Имя пользователя.
 * @param password Пароль пользователя.
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для получения данных авторизации пользователя.
 */
@Builder
public record LoginUser(
        @NotNull
        @Length(min = Constants.MIN_USERNAME_LENGTH, max = Constants.MAX_USERNAME_LENGTH)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username,

        @NotNull
        @Length(min = Constants.MIN_PASSWORD_LENGTH, max = Constants.MAX_PASSWORD_LENGTH)
        @Schema(defaultValue = "123", description = "Password")
        String password
) implements Serializable {
}
