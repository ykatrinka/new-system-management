package ru.clevertec.commentsservice.exception;


/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется когда возникает ошибка доступа к другому сервису.
 */
public class FeignServerErrorException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private FeignServerErrorException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param message Сообщение пользователю.
     * @return Объект выброшенного исключения.
     */
    public static FeignServerErrorException getInstance(String message) {
        return new FeignServerErrorException(message);
    }
}
