package ru.clevertec.newsservice.util;

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
    public static final String NEWS_ADD_TO_CACHE = "News with id {} was added to cache";
    public static final String NEWS_REMOVE_FROM_CACHE = "News with id {} was removed from cache";
    public static final String NEWS_GET_FROM_CACHE = "News with id {} was got from cache";

    public static final String LOG_MESSAGE_BEFORE = "Received call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_RETURNING = "Returning call :: method {} parameters {}";
    public static final String LOG_MESSAGE_AFTER_THROWING = "Throw exception :: method {} message {}";
    public static final String EMPTY_STRING = "";
}
