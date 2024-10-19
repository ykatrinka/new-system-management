package ru.clevertec.authservice.exception;


import ru.clevertec.authservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение выбрасывается когда имя пользователя не уникально.
 */
public class UserNotUniqueException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private UserNotUniqueException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param username Имя пользователя.
     * @return Объект выброшенного исключения.
     */
    public static UserNotUniqueException getByUsername(String username) {
        return new UserNotUniqueException(
                String.format(Constants.ERROR_USER_NOT_UNIQUE, username)
        );
    }
}
