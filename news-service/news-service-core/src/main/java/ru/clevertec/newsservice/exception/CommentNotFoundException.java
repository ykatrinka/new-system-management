package ru.clevertec.newsservice.exception;


import ru.clevertec.newsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется при не найденном идентификаторе комментария.
 */
public class CommentNotFoundException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private CommentNotFoundException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param id Не корректный идентификатор комментария.
     * @return Объект выброшенного исключения.
     */
    public static CommentNotFoundException getById(Long id) {
        return new CommentNotFoundException(Constants.ERROR_NO_SUCH_COMMENT + id);
    }
}
