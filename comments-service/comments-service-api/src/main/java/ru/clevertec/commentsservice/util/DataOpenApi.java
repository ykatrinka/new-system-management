package ru.clevertec.commentsservice.util;

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
    public static final String TAG_COMMENTS = "Comments Controller";
    public static final String SUMMARY_ADD_COMMENT = "Create comment";
    public static final String SUMMARY_GET_ALL_COMMENTS = "Get all comments";
    public static final String SUMMARY_GET_COMMENT = "Get comment by id";
    public static final String SUMMARY_UPDATE_COMMENT = "Update comment";
    public static final String SUMMARY_DELETE_COMMENT = "Delete comment by id";
    public static final String SUMMARY_SEARCH_COMMENTS = "Full text search comments";
    public static final String TAG_NEWS_FEIGN = "Feign News";
    public static final String SUMMARY_DELETE_COMMENTS_BY_NEWS_ID = "Delete all comments by news id";
    public static final String SUMMARY_GET_COMMENTS_BY_NEWS_ID = "Get comments by news id";

}
