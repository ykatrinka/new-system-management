package ru.clevertec.commentsservice.util;

import lombok.experimental.UtilityClass;

/**
 * Base endpoints.
 *
 * @author Katerina
 * @version 1.0.0
 */
@UtilityClass
public class BaseURLData {
    public static final String BASE_URL_COMMENTS = "/comments";
    public static final String URL_COMMENT_ID = "/{commentId}";
    public static final String URL_SEARCH = "search";
    public static final String URL_NEWS_NEWS_ID = "news/{newsId}";
    public static final String URL_NEWS_ID_COMMENTS = "/{newsId}/comments";

    public static final String PAGE_NUMBER = "pageNumber";
    public static final String FIRST_PAGE = "1";
    public static final String COMMENT_ID = "commentId";
    public static final String NEWS_ID = "newsId";

    public static final String SEARCH_VALUE_PARAM = "text";
    public static final String SEARCH_LIMIT_PARAM = "limit";
    public static final String SEARCH_LIMIT = "15";
    public static final String SEARCH_FIELD_PARAM = "fields";

}
