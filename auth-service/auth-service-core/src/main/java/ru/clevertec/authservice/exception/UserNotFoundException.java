package ru.clevertec.authservice.exception;


import ru.clevertec.authservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение выбрасывается при не найденном пользователе.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param username Имя пользователя.
     * @return Объект выброшенного исключения.
     */
    public static UserNotFoundException getByUsername(String username) {
        return new UserNotFoundException(
                String.format(Constants.ERROR_USER_NOT_FOUND, username)
        );
    }
}
