package ru.clevertec.newsservice.exception;


/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется в случае попытки изменения не своих данных.
 */
public class AnotherAuthorException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private AnotherAuthorException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param message сообщение пользователю.
     * @return Объект выброшенного исключения.
     */
    public static AnotherAuthorException getInstance(String message) {
        return new AnotherAuthorException(message);
    }
}
