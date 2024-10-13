package ru.clevertec.commentsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Сообщения при логировании.
 *
 * @author Katerina
 * @version 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggingMessage {
    public static final String COMMENT_ADD_TO_CACHE = "Comment with id {} was added to cache";
    public static final String COMMENT_REMOVE_FROM_CACHE = "Comment with id {} was removed from cache";
    public static final String COMMENT_GET_FROM_CACHE = "Comment with id {} was got from cache";

    public static final String LOG_MESSAGE_BEFORE = "Received call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_RETURNING = "Returning call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_THROWING = "Throw exception :: method {} message {}";
    public static final String EMPTY_STRING = "";
}
