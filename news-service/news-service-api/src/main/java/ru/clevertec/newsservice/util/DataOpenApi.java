package ru.clevertec.newsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Swagger open API.
 *
 * @author Katerina
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataOpenApi {

    public static final String TAG_NEWS = "News Controller";
    public static final String SUMMARY_ADD_NEWS = "Create news";
    public static final String SUMMARY_GET_ALL_NEWS = "Get all news";
    public static final String SUMMARY_GET_NEWS = "Get news by id";
    public static final String SUMMARY_UPDATE_NEWS = "Update news";
    public static final String SUMMARY_DELETE_NEWS = "Delete news by id";
    public static final String SUMMARY_SEARCH_NEWS = "Full text search news";
    public static final String TAG_COMMENTS_FEIGN = "Feign Comments";
    public static final String SUMMARY_GET_NEWS_WITH_COMMENTS = "Get news with comments";
    public static final String SUMMARY_GET_COMMENT = "Get comment by id";

}
