package ru.clevertec.commentsservice.exception;

import ru.clevertec.commentsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется, когда поле для полнотекстового поиска не найдено.
 */
public class NoSuchSearchFieldException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private NoSuchSearchFieldException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @return Объект выброшенного исключения.
     */
    public static NoSuchSearchFieldException getInstance() {
        return new NoSuchSearchFieldException(Constants.ERROR_NO_SUCH_FIELD);
    }
}
