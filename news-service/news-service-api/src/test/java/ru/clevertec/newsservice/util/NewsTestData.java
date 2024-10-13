package ru.clevertec.newsservice.util;

import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.CommentResponse;
import ru.clevertec.newsservice.dto.response.NewsCommentsResponse;
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

    public static final String USERNAME = "Patrik";
    public static final String COMMENT_TEXT = "This is a comment";
    public static final Long COMMENT_ID = 1L;

    public static final int SEARCH_LIMIT = 10;
    public static final String SEARCH_VALUE = "News";
    public static final List<String> SEARCH_FIELDS = List.of("title", "text");
    public static final List<String> SEARCH_NOT_VALID_FIELDS = List.of("username", "text");
    public static final String[] SEARCH_FIELDS_ARRAY = {"title", "text"};
    public static final String[] SEARCH_NOT_VALID_FIELDS_ARRAY = {"username", "text"};


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

    //search
    public static List<NewsResponse> getListNewsResponse() {
        return List.of(
                NewsResponse.builder()
                        .id(NEWS_ID)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_TEXT)
                        .build(),
                NewsResponse.builder()
                        .id(NEWS_ID)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_TEXT)
                        .build()
        );
    }


    public static List<CommentResponse> getListCommentsResponse() {
        return List.of(
                CommentResponse.builder()
                        .id(1L)
                        .username(USERNAME)
                        .newsId(NEWS_ID)
                        .text(COMMENT_TEXT)
                        .time(CREATED_DATE)
                        .build(),
                CommentResponse.builder()
                        .id(2L)
                        .username(USERNAME)
                        .newsId(NEWS_ID)
                        .text(COMMENT_TEXT)
                        .time(CREATED_DATE)
                        .build(),
                CommentResponse.builder()
                        .id(3L)
                        .username(USERNAME)
                        .newsId(NEWS_ID)
                        .text(COMMENT_TEXT)
                        .time(CREATED_DATE)
                        .build(),
                CommentResponse.builder()
                        .id(4L)
                        .username(USERNAME)
                        .newsId(NEWS_ID)
                        .text(COMMENT_TEXT)
                        .time(CREATED_DATE)
                        .build()
        );
    }

    public static NewsCommentsResponse getFillNewsResponseWithComments() {
        return NewsCommentsResponse.builder()
                .id(NEWS_ID)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_TEXT)
                .comments(
                        getListCommentsResponse()
                )
                .build();
    }

    public static CommentResponse getCommentResponse() {
        return CommentResponse.builder()
                .id(1L)
                .username(USERNAME)
                .newsId(NEWS_ID)
                .text(COMMENT_TEXT)
                .time(CREATED_DATE)
                .build();
    }
}
