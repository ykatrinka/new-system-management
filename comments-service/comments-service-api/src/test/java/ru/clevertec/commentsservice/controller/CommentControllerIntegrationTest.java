package ru.clevertec.commentsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.dto.response.NewsResponse;
import ru.clevertec.commentsservice.util.CommentTestData;

import java.net.URI;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ActiveProfiles("test")
@Sql(
        scripts = "classpath:testdata/add-test-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
@Testcontainers
@WireMockTest(httpPort = 8081)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    void shouldCreateCommentAndReturnStatus201() throws Exception {
        //given
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        HttpEntity<CommentRequest> httpRequest = new HttpEntity<>(commentRequest);
        NewsResponse newsResponse = CommentTestData.getNewsResponse();

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + commentRequest.newsId()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        objectMapper
                                                .writeValueAsString(newsResponse)
                                )
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                httpRequest,
                String.class);


        CommentResponse actualResponse = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertNotNull(actualResponse.id()),
                () -> assertEquals(CommentTestData.COMMENT_ID_NEW, actualResponse.id()),
                () -> assertEquals(commentRequest.username(), actualResponse.username()),
                () -> assertEquals(commentRequest.newsId(), actualResponse.newsId()),
                () -> assertEquals(commentRequest.text(), actualResponse.text()),
                () -> assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

    @Test
    void shouldNotCreateComment_whenNewsIdNotFound() {
        //given
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        HttpEntity<CommentRequest> httpRequest = new HttpEntity<>(commentRequest);

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + commentRequest.newsId()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(HttpStatus.NOT_FOUND.value())
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.POST,
                httpRequest,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

    @Test
    void shouldGetAllComments() throws JsonProcessingException {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments",
                HttpMethod.GET,
                null,
                String.class);


        //when
        List<CommentResponse> actualResponse = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(4, actualResponse.size()),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

    @Test
    void shouldNotGetComments_whenNoComments() {
        //given
        URI uri = UriComponentsBuilder.fromHttpUrl(restTemplate.getRootUri())
                .path("/comments")
                .queryParam("pageNumber", String.valueOf(2))
                .build().toUri();

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertNull(responseEntity.getBody()),
                () -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }


    @Test
    void shouldGetCommentById() throws Exception {
        //given
        long commentId = CommentTestData.COMMENT_ID;

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.GET,
                null,
                String.class);


        CommentResponse actualResponse = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(CommentTestData.USERNAME, actualResponse.username()),
                () -> assertEquals(CommentTestData.COMMENT_NEWS_ID, actualResponse.newsId()),
                () -> assertEquals(CommentTestData.COMMENT_TEXT, actualResponse.text()),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Test
    void shouldNotGetCommentById_whenCommentNotFound() {
        //given
        long commentId = CommentTestData.COMMENT_ID_NOT_FOUND;

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.GET,
                null,
                String.class);


        //then
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Test
    void shouldUpdateCommentById() throws Exception {
        //given
        long commentId = CommentTestData.COMMENT_ID_UPD;
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        HttpEntity<CommentRequest> httpRequest = new HttpEntity<>(commentRequest);
        NewsResponse newsResponse = CommentTestData.getNewsResponse();

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + commentRequest.newsId()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        objectMapper
                                                .writeValueAsString(newsResponse)
                                )
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.PUT,
                httpRequest,
                String.class);

        CommentResponse actualResponse = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertNotNull(actualResponse.id()),
                () -> assertEquals(CommentTestData.COMMENT_ID_UPD, actualResponse.id()),
                () -> assertEquals(commentRequest.username(), actualResponse.username()),
                () -> assertEquals(commentRequest.newsId(), actualResponse.newsId()),
                () -> assertEquals(commentRequest.text(), actualResponse.text()),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Test
    void shouldNotUpdateCommentById_whenNewsNotFound() {
        //given
        long commentId = CommentTestData.COMMENT_ID_UPD;
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        HttpEntity<CommentRequest> httpRequest = new HttpEntity<>(commentRequest);

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + commentRequest.newsId()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(HttpStatus.NOT_FOUND.value())
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.PUT,
                httpRequest,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );
    }

    @Test
    void shouldDeleteCommentById() {
        //given
        long commentId = CommentTestData.COMMENT_ID;

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + commentId,
                HttpMethod.DELETE,
                null,
                String.class);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldGetCommentsWithFullTextSearch() {
        //given
        String searchValue = CommentTestData.SEARCH_VALUE;
        List<String> searchFields = CommentTestData.SEARCH_FIELDS;
        int searchLimit = CommentTestData.SEARCH_LIMIT;

        //when
        URI uri = UriComponentsBuilder.fromHttpUrl(restTemplate.getRootUri())
                .path("/comments/search")
                .queryParam("text", searchValue)
                .queryParam("fields", searchFields)
                .queryParam("limit", String.valueOf(searchLimit))
                .build().toUri();


        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

    @Test
    void shouldNotGetCommentsWithFullTextSearch_whenSearchFieldsIsNotValid() {

        //given
        String searchValue = CommentTestData.SEARCH_VALUE;
        List<String> searchFields = CommentTestData.SEARCH_NOT_VALID_FIELDS;
        int searchLimit = CommentTestData.SEARCH_LIMIT;

        //when
        URI uri = UriComponentsBuilder.fromHttpUrl(restTemplate.getRootUri())
                .path("/comments/search")
                .queryParam("text", searchValue)
                .queryParam("fields", searchFields)
                .queryParam("limit", String.valueOf(searchLimit))
                .build().toUri();

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

    @Test
    void shouldDeleteCommentsByNewsId() {
        //given
        long newsId = CommentTestData.NEWS_ID;

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/news/" + newsId,
                HttpMethod.DELETE,
                null,
                String.class);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void shouldGetCommentsByNewsId() throws Exception {
        //given
        long newsId = CommentTestData.NEWS_ID;
        NewsResponse newsResponse = CommentTestData.getNewsResponse();

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + newsId))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        objectMapper
                                                .writeValueAsString(newsResponse)
                                )
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + newsId + "/comments",
                HttpMethod.GET,
                null,
                String.class);

        List<CommentResponse> actualResponse = objectMapper.readValue(
                responseEntity.getBody(),
                new TypeReference<>() {
                });

        //then
        assertAll(
                () -> assertNotNull(actualResponse),
                () -> assertEquals(2, actualResponse.size()),
                () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );


    }

    @Test
    void shouldGetEmptyListCommentsByNewsId() throws JsonProcessingException {
        //given
        long newsId = CommentTestData.NEWS_ID_NO_CONTENT;
        NewsResponse newsResponse = CommentTestData.getNewsResponse();

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + newsId))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withBody(
                                        objectMapper
                                                .writeValueAsString(newsResponse)
                                )
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + newsId + "/comments",
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }


    @Test
    void shouldNotGetListCommentsByNewsId_whenNewsIdNotFound() {
        //given
        long newsId = CommentTestData.NEWS_ID_NOT_FOUND;

        WireMock.stubFor(
                WireMock.get(urlPathEqualTo("/news/" + newsId))
                        .willReturn(aResponse()
                                .withHeader("Content-Type",
                                        MediaType.APPLICATION_JSON_VALUE)
                                .withStatus(HttpStatus.NOT_FOUND.value())
                        )
        );

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "/comments/" + newsId + "/comments",
                HttpMethod.GET,
                null,
                String.class);

        //then
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode()),
                () -> assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType())
        );

    }

}