package ru.clevertec.newsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.CommentResponse;
import ru.clevertec.newsservice.dto.response.NewsCommentsResponse;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

import static ru.clevertec.newsservice.util.BaseURLData.BASE_URL_NEWS;
import static ru.clevertec.newsservice.util.BaseURLData.COMMENT_ID_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.FIRST_PAGE;
import static ru.clevertec.newsservice.util.BaseURLData.NEWS_ID_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.PAGE_NUMBER_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.SEARCH_FIELD_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.SEARCH_LIMIT;
import static ru.clevertec.newsservice.util.BaseURLData.SEARCH_LIMIT_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.SEARCH_VALUE_PARAM;
import static ru.clevertec.newsservice.util.BaseURLData.URL_NEWS_ID;
import static ru.clevertec.newsservice.util.BaseURLData.URL_NEWS_ID_COMMENTS;
import static ru.clevertec.newsservice.util.BaseURLData.URL_NEWS_ID_COMMENTS_COMMENTS_ID;
import static ru.clevertec.newsservice.util.BaseURLData.URL_SEARCH;

/**
 * @author Katerina
 * @version 1.0
 * <p>
 * Контроллер для работы с новостями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL_NEWS)
public class NewsController {

    /**
     * Сервисный класс для работы с новостями.
     */
    private final NewsService newsService;

    /**
     * Endpoint для создания новой новости.
     *
     * @param newsRequest Данные из запроса на создание новости
     * @return NewsResponse
     * Созданная новость с новым сгенерированным id.
     */
    @PostMapping
    public ResponseEntity<NewsResponse> createNews(@Valid @RequestBody NewsRequest newsRequest) {
        NewsResponse news = newsService.createNews(newsRequest);
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    /**
     * Endpoint для получения списка новостей.
     * Используется пагинация.
     * Если не указан номер страницы, то используется первая (по умолчанию)
     *
     * @param pageNumber Номер страницы для получения списка.
     * @return Список новостей с учетом указанной страницы.
     */
    @GetMapping
    public ResponseEntity<List<NewsResponse>> getAllNews(
            @RequestParam(
                    name = PAGE_NUMBER_PARAM,
                    defaultValue = FIRST_PAGE,
                    required = false) int pageNumber) {
        List<NewsResponse> news = newsService.getAllNews(pageNumber - 1);
        return new ResponseEntity<>(news, news.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    /**
     * Endpoint для получения новости по id.
     *
     * @param newsId идентификатор получаемой новости
     * @return NewsResponse
     * Возвращает данные по новости.
     */
    @GetMapping(value = URL_NEWS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable(NEWS_ID_PARAM) Long newsId) {
        NewsResponse news = newsService.getNewsById(newsId);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * Endpoint для обновления данных новости.
     *
     * @param newsId      идентификатор обновляемой новости
     * @param newsRequest Данные из запроса для обновления новости.
     * @return NewsResponse
     * Обновленная новость.
     */
    @PutMapping("/{newsId}")
    public ResponseEntity<NewsResponse> updateNews(@PathVariable(NEWS_ID_PARAM) Long newsId,
                                                   @Valid @RequestBody NewsRequest newsRequest) {
        NewsResponse news = newsService.updateNews(newsId, newsRequest);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * Endpoint для удаления новости по идентификатору.
     *
     * @param newsId Идентификатор новости для удаления.
     */
    @DeleteMapping(URL_NEWS_ID)
    public void deleteNews(@PathVariable(NEWS_ID_PARAM) Long newsId) {
        newsService.deleteNews(newsId);
    }

    /**
     * Endpoint для полнотекстового поиска новостей.
     * Возвращает список новостей, соответствующих параметрам поиска.
     *
     * @param text   Текст для поиска.
     * @param limit  Необязательный параметр.
     *               Количество записей (по умолчанию 15).
     * @param fields Перечень полей для поиска.
     * @return Список новостей.
     */
    @GetMapping(URL_SEARCH)
    public ResponseEntity<List<NewsResponse>> searchNews(
            @RequestParam(name = SEARCH_VALUE_PARAM) String text,
            @RequestParam(
                    name = SEARCH_LIMIT_PARAM,
                    defaultValue = SEARCH_LIMIT,
                    required = false) int limit,
            @RequestParam(name = SEARCH_FIELD_PARAM) List<String> fields
    ) {
        return new ResponseEntity<>(newsService.searchNews(text, fields, limit), HttpStatus.OK);
    }

    /**
     * Endpoint для получения новости со списком комментариев.
     * Поиск по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber Номер страницы.
     * @return Список комментариев по указанной странице.
     */
    @GetMapping(value = URL_NEWS_ID_COMMENTS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsCommentsResponse> getNewsById(
            @PathVariable(NEWS_ID_PARAM) Long newsId,
            @RequestParam(
                    name = PAGE_NUMBER_PARAM,
                    defaultValue = FIRST_PAGE,
                    required = false) int pageNumber) {

        NewsCommentsResponse news = newsService.getNewsByIdWithComments(newsId, pageNumber);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * Получает комментарий по идентификатору.
     *
     * @param newsId    идентификатор новости.
     * @param commentId идентификатор комментария.
     * @return Комментарий.
     */
    @GetMapping(value = URL_NEWS_ID_COMMENTS_COMMENTS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable(NEWS_ID_PARAM) Long newsId,
            @PathVariable(COMMENT_ID_PARAM) Long commentId) {
        CommentResponse news = newsService.getNewsCommentById(newsId, commentId);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }
}
