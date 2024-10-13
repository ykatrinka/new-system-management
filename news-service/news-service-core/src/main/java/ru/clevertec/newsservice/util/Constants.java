package ru.clevertec.newsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Katerina
 * @version 1.0.0
 * Константы для работы с сервисом новостей.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    /**
     * Количество элементов на странице с новостями при пагинации.
     */
    public static final int NEWS_PAGE_SIZE = 4;
    /**
     * Сообщение об отсутствующем идентификаторе новости.
     */
    public static final String ERROR_NO_SUCH_NEWS = "No such News with id ";
    /**
     * Сообщение о не корректно указанном поле для полнотекстового поиска.
     */
    public static final String ERROR_NO_SUCH_FIELD = "No such search field";
    /**
     * Идентификатор комментария не соответствует выбранной новости.
     */
    public static final String ERROR_NO_MACH_NEWS_COMMENT = "No match News with id %s and comment with id %s";
    /**
     * Сообщение об отсутствующем идентификаторе комментария.
     */
    public static final String ERROR_NO_SUCH_COMMENT = "No such Comment with id ";

    public static final int MIN_TITLE_LENGTH = 3;
    public static final int MAX_TITLE_LENGTH = 255;

    public static final String CLIENT_NAME = "${spring.feign.client.comments-service.name}";
    public static final String CLIENT_URL = "${spring.feign.client.comments-service.url}";

    public static final String COMMENTS_NEWS_ID_COMMENTS = "/comments/{newsId}/comments";
    public static final String NEWS_ID_COMMENTS = "/{newsId}/comments";
    public static final String COMMENTS_COMMENTS_ID = "/comments/{commentId}";

    public static final String NEWS_ID = "newsId";
    public static final String COMMENT_ID = "commentId";

    public static final String PAGE_NUMBER_PARAM = "pageNumber";
    public static final String FIRST_PAGE = "1";
}
