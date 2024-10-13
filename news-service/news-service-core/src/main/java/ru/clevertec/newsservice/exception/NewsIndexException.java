package ru.clevertec.newsservice.exception;


/**
 * @author Katerina
 * @version 1.0.0
 * Исключение при работе с индексацией новостей.
 */
public class NewsIndexException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private NewsIndexException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param message Сообщение пользователю.
     * @return Объект выброшенного исключения.
     */
    public static NewsIndexException getInstance(String message) {
        return new NewsIndexException(message);
    }
}
