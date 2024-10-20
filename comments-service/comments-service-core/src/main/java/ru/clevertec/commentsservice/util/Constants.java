package ru.clevertec.commentsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Katerina
 * @version 1.0.0
 * Константы для работы с сервисом комментариев.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {
    /**
     * Количество элементов на странице комментариев при пагинации.
     */
    public static final int COMMENTS_PAGE_SIZE = 10;
    /**
     * Сообщение об отсутствующем идентификаторе комментария.
     */
    public static final String ERROR_NO_SUCH_COMMENT = "No such Comment with id ";
    /**
     * Сообщение о не корректно указанном поле для полнотекстового поиска.
     */
    public static final String ERROR_NO_SUCH_FIELD = "No such search field";
    /**
     * Сообщение об отсутствующем идентификаторе новости.
     */
    public static final String ERROR_NO_SUCH_NEWS = "No such News with id ";
    /**
     * Сообщение, когда news service возвращает 500 ошибку.
     */
    public static final String ERROR_FEIGN_NEWS = "Server news error";

    /**
     * Сообщение о не корректно указанном поле для полнотекстового поиска.
     */
    public static final String ERROR_ANOTHER_AUTHOR = "You are not author";

    /**
     * URL для получения новости по идентификатору.
     */
    public static final String URL_NEWS_NEWS_ID = "/news/{newsId}";

    public static final String CLIENT_NAME = "${spring.feign.client.news-service.name}";
    public static final String CLIENT_URL = "${spring.feign.client.news-service.url}";

    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;

    public static final String NEWS_ID = "newsId";

    /**
     * Сообщение когда класс не получен.
     */
    public static final String ERROR_INVALID_CLASS = "Invalid class %s";
    /**
     * Сообщение о проблемах индексации.
     */
    public static final String ERROR_INDEX_INTERRUPTED = "Index interrupted";

    /**
     * Путь к классу комментариев.
     */
    public static final String PATH_COMMENT = "ru.clevertec.commentsservice.entity.Comment";

    /**
     * Тип применяемого кеширования.
     */
    public static final String CACHE_TYPE = "${cache.type}";

    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final int MAX_PASSWORD_LENGTH = 100;

}
