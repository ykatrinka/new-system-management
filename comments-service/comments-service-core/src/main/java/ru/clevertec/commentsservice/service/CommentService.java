package ru.clevertec.commentsservice.service;


import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;

import java.util.List;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Сервис, реализующий работу с комментариями.
 */
public interface CommentService {
    /**
     * Метод для добавления нового комментария в базу данных.
     *
     * @param commentRequest Полученные данные для создания комментария.
     * @return CommentResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    CommentResponse createComment(CommentRequest commentRequest);

    /**
     * Метод для получения списка комментариев с учетом пагинации из базы данных.
     *
     * @param pageNumber Номер страницы.
     * @return Список комментариев из базы данных.
     */
    List<CommentResponse> getAllComments(int pageNumber);

    /**
     * Метод для получения комментария по идентификатору из базы данных.
     *
     * @param commentsId идентификатор комментария для получения.
     * @return Найденный комментарий из базы данных.
     */
    CommentResponse getCommentById(Long commentsId);

    /**
     * Метод для обновления комментария в базе данных.
     *
     * @param commentId      идентификатор обновляемого комментария.
     * @param commentRequest Полученные данные для обновления комментария.
     * @return CommentResponse
     * Возвращает обновленная сущность.
     */
    CommentResponse updateComment(Long commentId, CommentRequest commentRequest);

    /**
     * Метод для удаления комментария по id из базы данных.
     *
     * @param commentId идентификатор удаляемого комментария.
     */
    void deleteComment(Long commentId);

    /**
     * Метод для полнотекстового поиска
     * по выбранным полям.
     *
     * @param text   Текст для поиска.
     * @param limit  Количество элементов.
     * @param fields Перечень полей для поиска.
     * @return Список комментариев.
     */
    List<CommentResponse> searchComments(String text, List<String> fields, int limit);

    /**
     * Удаляет все комментарии по идентификатору новости.
     *
     * @param newsId Идентификатор новости.
     */
    void deleteCommentsByNewsId(Long newsId);

    /**
     * Получает список комментариев
     * по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber Номер страницы.
     * @return Список комментариев по указанной странице.
     */
    List<CommentResponse> getCommentsByNewsId(long newsId, int pageNumber);
}
