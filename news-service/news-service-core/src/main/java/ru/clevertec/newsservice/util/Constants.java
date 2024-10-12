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
}
