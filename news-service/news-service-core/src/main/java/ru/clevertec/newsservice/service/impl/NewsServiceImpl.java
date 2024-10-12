package ru.clevertec.newsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.entity.News;
import ru.clevertec.newsservice.exception.NewsNotFoundException;
import ru.clevertec.newsservice.exception.NoSuchSearchFieldException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.Constants;
import ru.clevertec.newsservice.util.ReflectionUtil;

import java.util.List;

/**
 * @author Katerina
 * @version 1.0.0
 * Сервис, реализующий работу с новостями.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    /**
     * Метод для добавления новой новости в базу данных.
     *
     * @param newsRequest Полученные данные для создания новости.
     * @return NewsResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        News news = newsMapper.requestToNews(newsRequest);
        News savedNews = newsRepository.save(news);

        return newsMapper.newsToResponse(savedNews);
    }

    /**
     * Метод для получения списка новостей с учетом пагинации из базы данных.
     *
     * @param pageNumber Номер страницы.
     * @return Список новостей из базы данных.
     */
    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getAllNews(int pageNumber) {
        PageRequest pageable = PageRequest.of(pageNumber, Constants.NEWS_PAGE_SIZE);
        Page<News> pageNews = newsRepository.findAll(pageable);

        return pageNews.getContent().stream()
                .map(newsMapper::newsToResponse)
                .toList();
    }

    /**
     * Метод для получения новости по идентификатору из базы данных.
     *
     * @param newsId идентификатор новости для получения.
     * @return Найденная новость из базы данных.
     */
    @Transactional(readOnly = true)
    @Override
    public NewsResponse getNewsById(Long newsId) {
        return newsRepository.findById(newsId)
                .map(newsMapper::newsToResponse)
                .orElseThrow(() ->
                        NewsNotFoundException.getById(newsId)
                );
    }

    /**
     * Метод для обновления новости в базе данных.
     *
     * @param newsRequest Полученные данные для обновления новости.
     * @param newsId      идентификатор обновляемой новости.
     * @return NewsResponse
     * Возвращает обновленную сущность.
     */
    @Override
    public NewsResponse updateNews(Long newsId, NewsRequest newsRequest) {
        News news = newsRepository.findById(newsId)
                .map(updNews -> newsMapper.updateFromRequest(newsId, newsRequest))
                .orElseThrow(() ->
                        NewsNotFoundException.getById(newsId)
                );
        News updatedNews = newsRepository.save(news);

        return newsMapper.newsToResponse(updatedNews);
    }

    /**
     * Метод для удаления новости по id из базы данных.
     *
     * @param newsId идентификатор удаляемой новости.
     */
    @Override
    public void deleteNews(Long newsId) {
        newsRepository.findById(newsId).ifPresent(newsRepository::delete);
    }

    /**
     * Метод для полнотекстового поиска
     * по выбранным полям.
     *
     * @param text   Текст для поиска.
     * @param limit  Количество элементов.
     * @param fields Перечень полей для поиска.
     */
    @Override
    public List<NewsResponse> searchNews(String text, List<String> fields, int limit) {

        List<String> searchableFields = ReflectionUtil
                .getFieldsByAnnotation(News.class, FullTextField.class);
        List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

        boolean containsInvalidField = fieldsToSearchBy
                .stream()
                .anyMatch(f -> !searchableFields.contains(f));

        if (containsInvalidField) {
            throw NoSuchSearchFieldException.getInstance();
        }

        List<News> news = newsRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
        return news.stream()
                .map(newsMapper::newsToResponse)
                .toList();
    }
}
