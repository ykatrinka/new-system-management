package ru.clevertec.newsservice.exception;


import ru.clevertec.newsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Исключение используется, когда комментарий соответствует другой новости.
 */
public class NotMatchNewsCommentException extends RuntimeException {
    /**
     * Конструктор для вызова конструктора базового класса.
     *
     * @param message Сообщение для пользователя.
     */
    private NotMatchNewsCommentException(String message) {
        super(message);
    }

    /**
     * Метод для создания объекта с формированием сообщения.
     *
     * @param newsId    идентификатор новости.
     * @param commentId идентификатор комментария.
     * @return Объект выброшенного исключения.
     */
    public static NotMatchNewsCommentException getById(Long newsId, Long commentId) {
        return new NotMatchNewsCommentException(
                String.format(Constants.ERROR_NO_MACH_NEWS_COMMENT, newsId, commentId));
    }
}
