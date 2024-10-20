package ru.clevertec.commentsservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.dto.response.NewsResponse;
import ru.clevertec.commentsservice.entity.Comment;
import ru.clevertec.commentsservice.exception.AnotherAuthorException;
import ru.clevertec.commentsservice.exception.CommentNotFoundException;
import ru.clevertec.commentsservice.exception.FeignServerErrorException;
import ru.clevertec.commentsservice.exception.NewsNotFoundException;
import ru.clevertec.commentsservice.exception.NoSuchSearchFieldException;
import ru.clevertec.commentsservice.feignclient.NewsFeignClient;
import ru.clevertec.commentsservice.mapper.CommentMapper;
import ru.clevertec.commentsservice.repository.CommentRepository;
import ru.clevertec.commentsservice.service.CommentService;
import ru.clevertec.commentsservice.util.Constants;
import ru.clevertec.commentsservice.util.DataSecurity;
import ru.clevertec.commentsservice.util.ReflectionUtil;

import java.util.List;
import java.util.Optional;

/**
 * @author Katerina
 * @version 1.0.0
 * Сервис, реализующий работу с комментариями.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    /**
     * Класс репозитория для работы с базой данных.
     */
    private final CommentRepository commentRepository;
    /**
     * Mapper для конвертации сущностей комментария.
     */
    private final CommentMapper commentMapper;
    /**
     * Open feign client for get data in news service.
     */
    private final NewsFeignClient newsClient;

    /**
     * Метод для добавления нового комментария в базу данных.
     *
     * @param commentRequest Полученные данные для создания комментария.
     * @return CommentResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    @CachePut(value = "comment", key = "#result.id")
    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        if (isNotValidUser(commentRequest.username())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
        checkNewsIsExists(commentRequest.newsId());

        Comment comment = commentMapper.requestToComment(commentRequest);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.commentToResponse(savedComment);
    }

    /**
     * Метод для получения списка комментариев с учетом пагинации из базы данных.
     *
     * @param pageNumber Номер страницы.
     * @return Список комментариев из базы данных.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments(int pageNumber) {
        PageRequest pageable = PageRequest.of(pageNumber, Constants.COMMENTS_PAGE_SIZE);
        Page<Comment> pageComments = commentRepository.findAll(pageable);

        return pageComments.getContent().stream()
                .map(commentMapper::commentToResponse)
                .toList();
    }


    /**
     * Метод для получения комментария по идентификатору из базы данных.
     *
     * @param commentsId идентификатор комментария для получения.
     * @return Найденный комментарий из базы данных.
     */
    @Cacheable(value = "comment")
    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentsId) {
        return commentRepository.findById(commentsId)
                .map(commentMapper::commentToResponse)
                .orElseThrow(
                        () -> CommentNotFoundException.getById(commentsId)
                );
    }

    /**
     * Метод для обновления комментария в базе данных.
     *
     * @param commentId      идентификатор обновляемого комментария.
     * @param commentRequest Полученные данные для обновления комментария.
     * @return CommentResponse
     * Возвращает обновленная сущность.
     */
    @CachePut(value = "comment", key = "#result.id")
    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Optional<Comment> commentInDB = commentRepository.findById(commentId);
        if (commentInDB.isPresent() && isNotValidUser(commentInDB.get().getUsername())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
        checkNewsIsExists(commentRequest.newsId());

        Comment comment = commentRepository.findById(commentId)
                .map(updComment -> commentMapper.updateFromRequest(commentId, commentRequest))
                .orElseThrow(
                        () -> CommentNotFoundException.getById(commentId)
                );
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.commentToResponse(updatedComment);
    }

    /**
     * Метод для удаления комментария по id из базы данных.
     *
     * @param commentId идентификатор удаляемого комментария.
     */
    @CacheEvict(value = "comment")
    @Override
    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent() && isNotValidUser(comment.get().getUsername())) {
            throw AnotherAuthorException.getInstance(Constants.ERROR_ANOTHER_AUTHOR);
        }
        comment.ifPresent(commentRepository::delete);
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
    public List<CommentResponse> searchComments(String text, List<String> fields, int limit) {

        List<String> searchableFields = ReflectionUtil
                .getFieldsByAnnotation(Comment.class, FullTextField.class);
        List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

        boolean containsInvalidField = fieldsToSearchBy
                .stream()
                .anyMatch(f -> !searchableFields.contains(f));

        if (containsInvalidField) {
            throw NoSuchSearchFieldException.getInstance();
        }

        List<Comment> comments = commentRepository.searchBy(
                text, limit, fieldsToSearchBy.toArray(new String[0]));
        return comments.stream()
                .map(commentMapper::commentToResponse)
                .toList();
    }

    /**
     * Удаляет все комментарии по идентификатору новости.
     *
     * @param newsId Идентификатор новости.
     */
    @Override
    public void deleteCommentsByNewsId(Long newsId) {
        commentRepository.deleteAllByNewsId(newsId);
    }

    /**
     * Получает список комментариев
     * по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber Номер страницы.
     * @return Список комментариев по указанной странице.
     */
    @Transactional(readOnly = true)
    @Override
    public List<CommentResponse> getCommentsByNewsId(long newsId, int pageNumber) {
        checkNewsIsExists(newsId);

        PageRequest pageable = PageRequest.of(pageNumber, Constants.COMMENTS_PAGE_SIZE);
        Page<Comment> pageComments = commentRepository.findByNewsId(newsId, pageable);

        return pageComments.getContent().stream()
                .map(commentMapper::commentToResponse)
                .toList();
    }

    /**
     * Метод выполняет запрос к news-service.
     * Проверяется статус и наличие новости с указанным id.
     * В случаях не найденной новости или ошибки соединения
     * выбрасывается исключение с сообщением об ошибке.
     *
     * @param newsId Идентификатор новости.
     */
    private void checkNewsIsExists(Long newsId) {
        try {
            ResponseEntity<NewsResponse> newsResponse = newsClient.getNewsById(newsId);

            if (newsResponse.getStatusCode().is5xxServerError()) {
                throw FeignServerErrorException.getInstance(Constants.ERROR_FEIGN_NEWS);
            }

            if (newsResponse.getBody() == null) {
                throw NewsNotFoundException.getById(newsId);
            }

        } catch (FeignException.NotFound e) {
            throw NewsNotFoundException.getById(newsId);
        } catch (FeignException e) {
            throw FeignServerErrorException.getInstance(Constants.ERROR_FEIGN_NEWS);
        }
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
                .contains(new SimpleGrantedAuthority(DataSecurity.ROLE_SUBSCRIBER))) {
            return true;
        }

        return !username.equals(authentication.getPrincipal());

    }
}