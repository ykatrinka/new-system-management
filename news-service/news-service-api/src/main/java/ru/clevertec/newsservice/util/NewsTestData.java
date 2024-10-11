package ru.clevertec.newsservice.util;

import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;

import java.time.LocalDateTime;
import java.util.List;

public class NewsTestData {

    public static final Long NEWS_ID = 1L;
    public static final String NEWS_TITLE = "News Title";
    public static final String NEWS_TEXT = "News content for News Title";

    public static final LocalDateTime CREATED_DATE = LocalDateTime
            .of(2024, 9, 3, 0, 0, 0);

    //integration tests
    public static final Long NEWS_ID_NEW = 4L;
    public static final Long NEWS_ID_NOT_FOUND = 42L;
    public static final Long NEWS_ID_UPD = 2L;

    public static NewsRequest getNewsRequest() {
        return NewsRequest.builder()
                .time(null)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static NewsResponse getNewsResponse() {
        return NewsResponse.builder()
                .id(NEWS_ID)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .build();
    }

    public static List<NewsResponse> getListWithTwoCommentResponse() {
        return List.of(
                NewsResponse.builder()
                        .id(1L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_TEXT)
                        .build(),
                NewsResponse.builder()
                        .id(2L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_TEXT)
                        .build()
        );
    }
}
