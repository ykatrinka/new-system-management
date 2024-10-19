package ru.clevertec.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import ru.clevertec.authservice.entity.Role;
import ru.clevertec.authservice.util.Constants;

import java.io.Serializable;

/**
 * @param username Имя пользователя.
 * @param password Пароль пользователя.
 * @param role     Роль пользователя.
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для получения данных регистрации нового пользователя.
 */
@Builder
public record RegisterUser(
        @NotNull
        @Length(min = Constants.MIN_USERNAME_LENGTH, max = Constants.MAX_USERNAME_LENGTH)
        @Schema(defaultValue = "Patrik", description = "Username")
        String username,

        @NotNull
        @Length(min = Constants.MIN_PASSWORD_LENGTH, max = Constants.MAX_PASSWORD_LENGTH)
        @Schema(defaultValue = "123", description = "Password")
        String password,

        @NotNull
        @Schema(defaultValue = "SUBSCRIBER", description = "Role")
        Role role
) implements Serializable {
}
