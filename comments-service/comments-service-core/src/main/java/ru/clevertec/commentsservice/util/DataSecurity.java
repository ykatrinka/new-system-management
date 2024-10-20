package ru.clevertec.commentsservice.util;

import lombok.experimental.UtilityClass;

/**
 * @author Katerina
 * @version 1.0.0
 * Константы security.
 */
@UtilityClass
public class DataSecurity {

    /**
     * Ключ для формирования токена.
     */
    public static final String KEY_SECRET = "${security.jwt.secret-key}";

    public static final String CLIENT_NAME = "${spring.feign.client.auth-service.name}";
    public static final String CLIENT_URL = "${spring.feign.client.auth-service.url}";

    public static final String JWT_ROLE = "role";
    /**
     * Название параметра авторизации в заголовке.
     */
    public static final String HEADER_AUTH = "Authorization";
    public static final String HEADER_AUTH_TYPE = "Bearer ";

    /**
     * URL получение токена open feign.
     */
    public static final String URL_SEC_AUTH_TOKEN = "auth/token";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_SUBSCRIBER = "SUBSCRIBER";
}
