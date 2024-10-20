package ru.clevertec.authservice.util;

import lombok.experimental.UtilityClass;

/**
 * @author Katerina
 * @version 1.0.0
 * Константы для работы с сервисом авторизации.
 */
@UtilityClass
public class Constants {
    /**
     * Ключ для формирования токена.
     */
    public static final String KEY_SECRET = "${security.jwt.secret-key}";

    /**
     * Время действия токена
     */
    public static final String EXPIRATION_TIME = "${security.jwt.expiration-time}";

    /**
     * Сообщение об отсутствующем пользователе.
     */
    public static final String ERROR_USER_NOT_FOUND = "User not found %s";

    /**
     * Сообщение о не уникальном пользователе.
     */
    public static final String ERROR_USER_NOT_UNIQUE = "Username not unique %s";

    /**
     * Название параметра авторизации в заголовке.
     */
    public static final String HEADER_AUTH = "Authorization";

    /**
     * Тип авторизации.
     */
    public static final String BEARER_AUTH = "Bearer ";

    /**
     * URL auth.
     */
    public static final String URL_SEC_AUTH = "/auth/token";

    /**
     * URL регистрация нового пользователя.
     */
    public static final String URL_SEC_REG = "/auth/register";

    /**
     * URL open API.
     */
    public static final String URL_SEC_OPEN_API = "/swagger-ui/**";

    /**
     * URL open API doc.
     */
    public static final String URL_SEC_OPEN_API_DOC = "v3/api-docs/**";

    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 30;

    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final int MAX_PASSWORD_LENGTH = 100;

    /**
     * Имя поля роли в токене.
     */
    public static final String JWT_ROLE = "role";

    public static final String ERROR_AUTH = "Не верно введено имя пользователя или пароль";
}
