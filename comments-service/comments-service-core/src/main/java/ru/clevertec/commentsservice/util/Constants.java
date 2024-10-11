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
}
