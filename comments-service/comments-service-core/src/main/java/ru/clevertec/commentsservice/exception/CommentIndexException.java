package ru.clevertec.commentsservice.exception;


/**
 * @author Katerina
 * @version 1.0.0
 * Исключение при работе с индексацией комментариев.
 */
public class CommentIndexException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private CommentIndexException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param message Сообщение пользователю.
     * @return Объект выброшенного исключения.
     */
    public static CommentIndexException getInstance(String message) {
        return new CommentIndexException(message);
    }
}
