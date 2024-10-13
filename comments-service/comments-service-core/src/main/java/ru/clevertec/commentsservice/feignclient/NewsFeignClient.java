package ru.clevertec.commentsservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.clevertec.commentsservice.dto.response.NewsResponse;

import static ru.clevertec.commentsservice.util.Constants.CLIENT_NAME;
import static ru.clevertec.commentsservice.util.Constants.CLIENT_URL;
import static ru.clevertec.commentsservice.util.Constants.NEWS_ID;
import static ru.clevertec.commentsservice.util.Constants.URL_NEWS_NEWS_ID;

/**
 * Класс для работы с сервисом новостей.
 *
 * @author Katerina
 * @version 1.0.0
 */
@FeignClient(
        name = CLIENT_NAME,
        url = CLIENT_URL
)
public interface NewsFeignClient {

    /**
     * Получает новость по идентификатору.
     *
     * @param newsId идентификатор новости.
     * @return новость.
     */
    @GetMapping(path = URL_NEWS_NEWS_ID)
    ResponseEntity<NewsResponse> getNewsById(@PathVariable(NEWS_ID) Long newsId);
}
