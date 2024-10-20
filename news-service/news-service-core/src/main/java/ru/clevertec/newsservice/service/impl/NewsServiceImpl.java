package ru.clevertec.newsservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.CommentResponse;
import ru.clevertec.newsservice.dto.response.NewsCommentsResponse;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.entity.News;
import ru.clevertec.newsservice.exception.AnotherAuthorException;
import ru.clevertec.newsservice.exception.CommentNotFoundException;
import ru.clevertec.newsservice.exception.FeignServerErrorException;
import ru.clevertec.newsservice.exception.NewsNotFoundException;
import ru.clevertec.newsservice.exception.NoSuchSearchFieldException;
import ru.clevertec.newsservice.exception.NotMatchNewsCommentException;
import ru.clevertec.newsservice.feignclient.CommentsFeignClient;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.Constants;
import ru.clevertec.newsservice.util.DataSecurity;
import ru.clevertec.newsservice.util.ReflectionUtil;

import java.util.List;
import java.util.Optional;

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
    private final CommentsFeignClient commentsClient;

    /**
     * Метод для добавления новой новости в базу данных.
     *
     * @param newsRequest Полученные данные для создания новости.
     * @return NewsResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    @CachePut(value = "news", key = "#result.id")
    @Override
    public NewsResponse createNews(NewsRequest newsRequest) {
        if (isNotValidUser(newsRequest.username())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
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
    @Cacheable(value = "news")
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
     * @param newsId      идентификатор обновляемой новости.
     * @param newsRequest Полученные данные для обновления новости.
     * @return NewsResponse
     * Возвращает обновленную сущность.
     */
    @CachePut(value = "news", key = "#result.id")
    @Override
    public NewsResponse updateNews(Long newsId, NewsRequest newsRequest) {
        Optional<News> newsInBD = newsRepository.findById(newsId);
        if (newsInBD.isPresent() && isNotValidUser(newsInBD.get().getUsername())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
        News news = newsInBD
                .map(updNews -> newsMapper.updateFromRequest(newsId, newsRequest))
                .orElseThrow(() ->
                        NewsNotFoundException.getById(newsId)
                );
        News updatedNews = newsRepository.save(news);

        return newsMapper.newsToResponse(updatedNews);
    }

    /**
     * Метод для удаления новости по id из базы данных.
     * Комментарии удаляются каскадно.
     *
     * @param newsId идентификатор удаляемой новости.
     */
    @CacheEvict(value = "news")
    @Override
    public void deleteNews(Long newsId) {
        Optional<News> news = newsRepository.findById(newsId);
        if (news.isPresent() && isNotValidUser(news.get().getUsername())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
        List<CommentResponse> comments = commentsClient.getCommentsByNewsId(newsId, 1);
        if (comments != null && comments.isEmpty()) {
            commentsClient.deleteCommentsByNewsId(newsId);
        }
        news.ifPresent(newsRepository::delete);
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

    /**
     * Получает новость со списком комментариев.
     * Поиск по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber Номер страницы.
     * @return Список комментариев по указанной странице.
     */
    @Transactional(readOnly = true)
    @Override
    public NewsCommentsResponse getNewsByIdWithComments(Long newsId, int pageNumber) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> NewsNotFoundException.getById(newsId));

        try {
            List<CommentResponse> comments = commentsClient.getCommentsByNewsId(newsId, pageNumber);
            return newsMapper.newsToCommentsResponse(news, comments);
        } catch (FeignException e) {
            throw FeignServerErrorException.getInstance(Constants.ERROR_FEIGN_COMMENTS);
        }
    }

    /**
     * Получает комментарий по идентификатору.
     *
     * @param newsId    идентификатор новости.
     * @param commentId идентификатор комментария.
     * @return Комментарий.
     */
    @Transactional(readOnly = true)
    @Override
    public CommentResponse getNewsCommentById(Long newsId, Long commentId) {
        Optional<News> news = newsRepository.findById(newsId);
        if (news.isEmpty()) {
            throw NewsNotFoundException.getById(newsId);
        }
        CommentResponse comment;
        try {
            comment = commentsClient.getCommentById(commentId);
        } catch (FeignException.NotFound e) {
            throw CommentNotFoundException.getById(commentId);
        } catch (FeignException e) {
            throw FeignServerErrorException.getInstance(Constants.ERROR_FEIGN_COMMENTS);
        }

        if (!newsId.equals(comment.newsId())) {
            throw NotMatchNewsCommentException.getById(newsId, commentId);
        }

        return comment;
    }

    /**
     * Метод проверяет, является ли пользователь автором
     * или администратором.
     *
     * @param username автор.
     * @return true - если не автор и не администратор.
     */
    private boolean isNotValidUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(DataSecurity.ROLE_ADMIN))) {
            return false;
        }

        if (!authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(DataSecurity.ROLE_JOURNALIST))) {
            return true;
        }

        return !username.equals(authentication.getPrincipal());
    }
}
