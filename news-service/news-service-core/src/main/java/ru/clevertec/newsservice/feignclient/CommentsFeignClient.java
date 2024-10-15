package ru.clevertec.newsservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clevertec.newsservice.dto.response.CommentResponse;

import java.util.List;

import static ru.clevertec.newsservice.util.Constants.CLIENT_NAME;
import static ru.clevertec.newsservice.util.Constants.CLIENT_URL;
import static ru.clevertec.newsservice.util.Constants.COMMENTS_COMMENTS_ID;
import static ru.clevertec.newsservice.util.Constants.COMMENTS_NEWS_ID_COMMENTS;
import static ru.clevertec.newsservice.util.Constants.COMMENTS_NEWS_NEWS_ID;
import static ru.clevertec.newsservice.util.Constants.COMMENT_ID;
import static ru.clevertec.newsservice.util.Constants.FIRST_PAGE;
import static ru.clevertec.newsservice.util.Constants.NEWS_ID;
import static ru.clevertec.newsservice.util.Constants.PAGE_NUMBER_PARAM;

/**
 * Класс для работы с сервисом комментариев.
 *
 * @author Katerina
 * @version 1.0.0
 */
@FeignClient(
        name = CLIENT_NAME,
        url = CLIENT_URL
)
public interface CommentsFeignClient {

    /**
     * Получает список комментариев по идентификатору новости (с учетом пагинации).
     *
     * @param newsId     идентификатор новости.
     * @param pageNumber номер страницы.
     * @return список комментариев.
     */
    @GetMapping(path = COMMENTS_NEWS_ID_COMMENTS)
    List<CommentResponse> getCommentsByNewsId(@PathVariable(NEWS_ID) Long newsId,
                                              @RequestParam(
                                                      name = PAGE_NUMBER_PARAM,
                                                      defaultValue = FIRST_PAGE,
                                                      required = false) int pageNumber);

    /**
     * Получает комментарий по идентификатору.
     *
     * @param commentId идентификатор комментария.
     * @return комментарий.
     */
    @GetMapping(path = COMMENTS_COMMENTS_ID)
    CommentResponse getCommentById(@PathVariable(COMMENT_ID) Long commentId);

    /**
     * Удаляет список комментариев по идентификатору новости.
     *
     * @param newsId идентификатор новости.
     */
    @DeleteMapping(path = COMMENTS_NEWS_NEWS_ID)
    void deleteCommentsByNewsId(@PathVariable(NEWS_ID) Long newsId);
}
