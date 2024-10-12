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
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.service.NewsService;

import java.util.List;

/**
 * @author Katerina
 * @version 1.0
 * <p>
 * Контроллер для работы с новостями.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
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
                    name = "pageNumber",
                    defaultValue = "1",
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
    @GetMapping(value = "/{newsId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable("newsId") Long newsId) {
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
    public ResponseEntity<NewsResponse> updateNews(@PathVariable("newsId") Long newsId,
                                                   @Valid @RequestBody NewsRequest newsRequest) {
        NewsResponse news = newsService.updateNews(newsId, newsRequest);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * Endpoint для удаления новости по идентификатору.
     *
     * @param newsId Идентификатор новости для удаления.
     */
    @DeleteMapping("/{newsId}")
    public void deleteNews(@PathVariable("newsId") Long newsId) {
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
     */
    @GetMapping("/search")
    public ResponseEntity<List<NewsResponse>> searchNews(
            @RequestParam(name = "text") String text,
            @RequestParam(
                    name = "limit",
                    defaultValue = "15",
                    required = false) int limit,
            @RequestParam(name = "fields") List<String> fields
    ) {
        return new ResponseEntity<>(newsService.searchNews(text, fields, limit), HttpStatus.OK);
    }
}
