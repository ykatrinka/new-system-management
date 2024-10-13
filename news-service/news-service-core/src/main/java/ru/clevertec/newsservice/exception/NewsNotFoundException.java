package ru.clevertec.newsservice.exception;

import ru.clevertec.newsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется при не найденном идентификаторе новости.
 */
public class NewsNotFoundException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private NewsNotFoundException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param id Не корректный идентификатор новости.
     * @return Объект выброшенного исключения.
     */
    public static NewsNotFoundException getById(Long id) {
        return new NewsNotFoundException(Constants.ERROR_NO_SUCH_NEWS + id);
    }
}
