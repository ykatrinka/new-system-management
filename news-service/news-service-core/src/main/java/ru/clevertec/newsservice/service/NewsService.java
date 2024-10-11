package ru.clevertec.newsservice.service;


import ru.clevertec.newsservice.dto.request.NewsRequest;
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

}
