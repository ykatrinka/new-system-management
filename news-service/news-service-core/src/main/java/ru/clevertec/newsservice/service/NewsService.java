package ru.clevertec.newsservice.service;


import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.CommentResponse;
import ru.clevertec.newsservice.dto.response.NewsCommentsResponse;
import ru.clevertec.newsservice.dto.response.NewsResponse;

import java.util.List;

/**
 * @author Katerina
 * @version 1.0.0
 * Сервис, реализующий работу с новостями.
 */
public interface NewsService {

    /**
     * Метод для добавления новой новости в базу данных.
     *
     * @param newsRequest Полученные данные для создания новости.
     * @return NewsResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    NewsResponse createNews(NewsRequest newsRequest);

    /**
     * Метод для получения списка новостей с учетом пагинации из базы данных.
     *
     * @param pageNumber Номер страницы.
     * @return Список новостей из базы данных.
     */
    List<NewsResponse> getAllNews(int pageNumber);

    /**
     * Метод для получения новости по идентификатору из базы данных.
     *
     * @param newsId идентификатор новости для получения.
     * @return Найденная новость из базы данных.
     */
    NewsResponse getNewsById(Long newsId);

    /**
     * Метод для обновления новости в базе данных.
     *
     * @param newsRequest Полученные данные для обновления новости.
     * @param newsId      идентификатор обновляемой новости.
     * @return NewsResponse
     * Возвращает обновленную сущность.
     */
    NewsResponse updateNews(Long newsId, NewsRequest newsRequest);

    /**
     * Метод для удаления новости по id из базы данных.
     *
     * @param newsId идентификатор удаляемой новости.
     */
    void deleteNews(Long newsId);

    /**
     * Метод для полнотекстового поиска
     * по выбранным полям.
     *
     * @param text   Текст для поиска.
     * @param limit  Количество элементов.
     * @param fields Перечень полей для поиска.
     * @return Список новостей.
     */
    List<NewsResponse> searchNews(String text, List<String> fields, int limit);

    /**
     * Получает новость со списком комментариев.
     * Поиск по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber Номер страницы.
     * @return Список комментариев по указанной странице.
     */
    @Transactional(readOnly = true)
    NewsCommentsResponse getNewsByIdWithComments(Long newsId, int pageNumber);

    /**
     * Получает комментарий по идентификатору.
     *
     * @param newsId    идентификатор новости.
     * @param commentId идентификатор комментария.
     * @return Комментарий.
     */
    @Transactional(readOnly = true)
    CommentResponse getNewsCommentById(Long newsId, Long commentId);
}
