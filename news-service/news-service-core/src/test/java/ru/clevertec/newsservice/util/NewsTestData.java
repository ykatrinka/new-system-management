package ru.clevertec.newsservice.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.entity.News;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class NewsTestData {

    public static final String NEWS_TITLE = "News Title";
    public static final String NEWS_CONTENT = "News content for News Title";

    public static final LocalDateTime CREATED_DATE = LocalDateTime
            .of(2024, 9, 3, 0, 0, 0);
    public static final int PAGE_SIZE_NEWS = 4;
    public static final int PAGE_NUMBER_NEWS = 1;

    public static final long NEWS_ID_FOR_GET = 1L;
    public static final long NEWS_ID_FOR_DELETE = 1L;

    public static final long NEWS_ID_FOR_UPDATE = 1L;
    public static final LocalDateTime UPDATED_DATE = LocalDateTime
            .of(2024, 8, 15, 0, 0, 0);
    public static final String NEWS_CONTENT_UPDATE = "Updated news content for News Title";

    public static final String TITLE_SEARCH = "News Title";
    public static final String TEXT_SEARCH = "News content for News Title";
    public static final String SEARCH_VALUE = "News";
    public static final List<String> SEARCH_FIELDS = List.of("title", "text");
    public static final List<String> SEARCH_NOT_VALID_FIELDS = List.of("username", "text");
    public static final String[] SEARCH_FIELDS_ARRAY = {"title", "text"};

    //  create
    public static NewsRequest getNewsRequestForCreate() {
        return NewsRequest.builder()
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    public static News getNewsForCreate() {
        return News.builder()
                .id(null)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    public static News getFillNewsForCreate() {
        return News.builder()
                .id(1L)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    public static NewsResponse getNewsResponseForCreate() {
        return NewsResponse.builder()
                .id(1L)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    //  get all
    public static Page<News> getPageableListNews() {
        List<News> news = List.of(
                News.builder()
                        .id(1L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                News.builder()
                        .id(2L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                News.builder()
                        .id(3L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                News.builder()
                        .id(4L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build()
        );
        return new PageImpl<>(news);
    }

    public static List<NewsResponse> getListNewsResponses() {
        return List.of(
                NewsResponse.builder()
                        .id(1L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                NewsResponse.builder()
                        .id(2L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                NewsResponse.builder()
                        .id(3L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                NewsResponse.builder()
                        .id(4L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build()
        );
    }

    //  get by id
    public static News getFillNewsForGetById() {
        return News.builder()
                .id(NEWS_ID_FOR_GET)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    public static NewsResponse getFillNewsResponseForGetById() {
        return NewsResponse.builder()
                .id(NEWS_ID_FOR_GET)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    //  update
    public static NewsRequest getNewsRequestForUpdate() {
        return NewsRequest.builder()
                .time(UPDATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT_UPDATE)
                .build();
    }

    public static Optional<News> getNewsForUpdate() {
        return Optional.of(
                News.builder()
                        .time(UPDATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT_UPDATE)
                        .build()
        );
    }

    public static News getUpdatedNewsForUpdate() {
        return News.builder()
                .id(NEWS_ID_FOR_UPDATE)
                .time(UPDATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT_UPDATE)
                .build();
    }

    public static NewsResponse getNewsResponseForUpdate() {
        return NewsResponse.builder()
                .id(NEWS_ID_FOR_UPDATE)
                .time(UPDATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT_UPDATE)
                .build();
    }

    //mapper
    public static NewsRequest getFillNewsRequest() {
        return NewsRequest.builder()
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    public static News getFillNewsWithId() {
        return News.builder()
                .id(NEWS_ID_FOR_GET)
                .time(CREATED_DATE)
                .title(NEWS_TITLE)
                .text(NEWS_CONTENT)
                .build();
    }

    //  full text search
    public static List<News> getListNewsSearch() {
        return List.of(
                News.builder()
                        .id(1L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build(),
                News.builder()
                        .id(2L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(NEWS_CONTENT)
                        .build()
        );
    }

    public static List<NewsResponse> getListNewsResponseSearch() {
        return List.of(
                NewsResponse.builder()
                        .id(1L)
                        .time(CREATED_DATE)
                        .title(TITLE_SEARCH)
                        .text(NEWS_CONTENT)
                        .build(),
                NewsResponse.builder()
                        .id(2L)
                        .time(CREATED_DATE)
                        .title(NEWS_TITLE)
                        .text(TEXT_SEARCH)
                        .build()
        );
    }

}
