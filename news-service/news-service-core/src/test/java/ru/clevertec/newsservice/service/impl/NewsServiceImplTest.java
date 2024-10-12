package ru.clevertec.newsservice.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.entity.News;
import ru.clevertec.newsservice.exception.NewsNotFoundException;
import ru.clevertec.newsservice.exception.NoSuchSearchFieldException;
import ru.clevertec.newsservice.mapper.NewsMapper;
import ru.clevertec.newsservice.repository.NewsRepository;
import ru.clevertec.newsservice.util.NewsTestData;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsServiceImpl newsService;


    @Test
    void shouldCreateNews() {
        //given
        NewsRequest newsRequest = NewsTestData.getNewsRequestForCreate();
        News news = NewsTestData.getNewsForCreate();
        News createdNews = NewsTestData.getFillNewsForCreate();
        NewsResponse newsResponse = NewsTestData.getNewsResponseForCreate();

        when(newsMapper.requestToNews(newsRequest))
                .thenReturn(news);
        when(newsRepository.save(news))
                .thenReturn(createdNews);
        when(newsMapper.newsToResponse(createdNews))
                .thenReturn(newsResponse);

        //when
        NewsResponse actualNewsResponse = newsService.createNews(newsRequest);

        //then
        assertAll(
                () -> assertNotNull(actualNewsResponse),
                () -> assertNotNull(actualNewsResponse.id()),
                () -> assertNotNull(actualNewsResponse.time()),
                () -> assertEquals(NewsTestData.NEWS_TITLE, actualNewsResponse.title()),
                () -> assertEquals(NewsTestData.NEWS_CONTENT, actualNewsResponse.text())
        );

        verify(newsMapper, times(1)).requestToNews(newsRequest);
        verify(newsRepository, times(1)).save(news);
        verify(newsMapper, times(1)).newsToResponse(createdNews);
    }

    @Test
    void shouldGetAllNews() {
        //given
        PageRequest pageable = PageRequest.of(
                NewsTestData.PAGE_NUMBER_NEWS,
                NewsTestData.PAGE_SIZE_NEWS
        );
        Page<News> pageNews = NewsTestData.getPageableListNews();
        List<NewsResponse> newsResponses = NewsTestData.getListNewsResponses();

        when(newsRepository.findAll(pageable))
                .thenReturn(pageNews);
        when(newsMapper.newsToResponse(pageNews.getContent().get(0)))
                .thenReturn(newsResponses.get(0));
        when(newsMapper.newsToResponse(pageNews.getContent().get(1)))
                .thenReturn(newsResponses.get(1));
        when(newsMapper.newsToResponse(pageNews.getContent().get(2)))
                .thenReturn(newsResponses.get(2));
        when(newsMapper.newsToResponse(pageNews.getContent().get(3)))
                .thenReturn(newsResponses.get(3));

        //when
        List<NewsResponse> actualNews = newsService.getAllNews(NewsTestData.PAGE_NUMBER_NEWS);

        //then
        assertEquals(newsResponses.size(), actualNews.size());

        verify(newsRepository, times(1)).findAll(pageable);
        verify(newsMapper, times(1)).newsToResponse(pageNews.getContent().get(0));
        verify(newsMapper, times(1)).newsToResponse(pageNews.getContent().get(1));
        verify(newsMapper, times(1)).newsToResponse(pageNews.getContent().get(2));
        verify(newsMapper, times(1)).newsToResponse(pageNews.getContent().get(3));
    }

    @Nested
    class getById {

        @Test
        void shouldGetNewsById() {
            //given
            Long newsId = NewsTestData.NEWS_ID_FOR_GET;
            News news = NewsTestData.getFillNewsForGetById();
            NewsResponse newsResponse = NewsTestData.getFillNewsResponseForGetById();

            when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
            when(newsMapper.newsToResponse(news)).thenReturn(newsResponse);

            //when
            NewsResponse actualNews = newsService.getNewsById(newsId);

            //then
            assertAll(
                    () -> assertNotNull(actualNews),
                    () -> assertEquals(newsId, actualNews.id()),
                    () -> assertEquals(NewsTestData.CREATED_DATE, actualNews.time()),
                    () -> assertEquals(NewsTestData.NEWS_TITLE, actualNews.title()),
                    () -> assertEquals(NewsTestData.NEWS_CONTENT, actualNews.text())
            );

            verify(newsRepository, times(1)).findById(newsId);
            verify(newsMapper, times(1)).newsToResponse(news);
        }

        @Test
        void shouldNotGetNewsById_whenNewsByIdNotFound() {
            //given
            Long newsId = NewsTestData.NEWS_ID_FOR_GET;
            when(newsRepository.findById(newsId))
                    .thenThrow(NewsNotFoundException.getById(newsId));

            //when, then
            assertThrows(NewsNotFoundException.class,
                    () -> newsService.getNewsById(newsId));

            verify(newsRepository, times(1))
                    .findById(newsId);
            verify(newsMapper, times(0))
                    .newsToResponse(any());
        }
    }

    @Nested
    class update {
        @Test
        void shouldUpdateNews() {
            //given
            long newsId = NewsTestData.NEWS_ID_FOR_UPDATE;
            NewsRequest newsRequest = NewsTestData.getNewsRequestForUpdate();
            Optional<News> news = NewsTestData.getNewsForUpdate();
            News updatedNews = NewsTestData.getUpdatedNewsForUpdate();
            NewsResponse newsResponse = NewsTestData.getNewsResponseForUpdate();

            when(newsRepository.findById(newsId))
                    .thenReturn(news);
            when(newsMapper.updateFromRequest(newsId, newsRequest))
                    .thenReturn(updatedNews);
            when(newsRepository.save(updatedNews)).thenReturn(updatedNews);
            when(newsMapper.newsToResponse(updatedNews)).thenReturn(newsResponse);

            //when
            NewsResponse actualNews = newsService.updateNews(newsId, newsRequest);

            //then
            assertAll(
                    () -> assertNotNull(actualNews),
                    () -> assertEquals(NewsTestData.NEWS_ID_FOR_UPDATE, actualNews.id()),
                    () -> assertEquals(NewsTestData.UPDATED_DATE, actualNews.time()),
                    () -> assertEquals(NewsTestData.NEWS_TITLE, actualNews.title()),
                    () -> assertEquals(NewsTestData.NEWS_CONTENT_UPDATE, actualNews.text())
            );


            verify(newsRepository, times(1))
                    .findById(newsId);
            verify(newsMapper, times(1))
                    .updateFromRequest(newsId, newsRequest);
            verify(newsRepository, times(1))
                    .save(updatedNews);
            verify(newsMapper, times(1))
                    .newsToResponse(updatedNews);
        }

        @Test
        void shouldNotUpdateNews_whenNewsNotFound() {
            //given
            long newsId = NewsTestData.NEWS_ID_FOR_UPDATE;
            NewsRequest newsRequest = NewsTestData.getNewsRequestForUpdate();

            when(newsRepository.findById(newsId))
                    .thenReturn(Optional.empty());


            //when, then
            assertThrows(NewsNotFoundException.class,
                    () -> newsService.updateNews(newsId, newsRequest));

            verify(newsRepository, times(1))
                    .findById(newsId);
        }

    }

    @Test
    void shouldDeleteNews() {
        //given
        long newsId = NewsTestData.NEWS_ID_FOR_DELETE;
        News news = NewsTestData.getFillNewsForGetById();

        //when
        when(newsRepository.findById(newsId))
                .thenReturn(Optional.of(news));

        //then
        newsService.deleteNews(newsId);
        verify(newsRepository, times(1)).findById(newsId);
        verify(newsRepository, times(1)).delete(news);
    }


    @Nested
    class search {

        @Test
        void shouldGetNewsWithFullTextSearch() {
            //given
            List<News> news = NewsTestData.getListNewsSearch();
            List<NewsResponse> newsResponses = NewsTestData.getListNewsResponseSearch();

            when(newsRepository.searchBy(NewsTestData.SEARCH_VALUE, 1,
                    NewsTestData.SEARCH_FIELDS_ARRAY))
                    .thenReturn(news);
            when(newsMapper.newsToResponse(news.get(0)))
                    .thenReturn(newsResponses.get(0));
            when(newsMapper.newsToResponse(news.get(1)))
                    .thenReturn(newsResponses.get(1));

            //when
            List<NewsResponse> actualNews = newsService.searchNews(
                    NewsTestData.SEARCH_VALUE,
                    NewsTestData.SEARCH_FIELDS,
                    1
            );

            //then
            assertEquals(newsResponses.size(), actualNews.size());
            verify(newsRepository, times(1))
                    .searchBy(NewsTestData.SEARCH_VALUE, 1,
                            NewsTestData.SEARCH_FIELDS_ARRAY);
            verify(newsMapper, times(2)).newsToResponse(any());
        }

        @Test
        void shouldGetNewsWithFullTextSearch_emptyFields() {
            //given
            List<News> news = NewsTestData.getListNewsSearch();
            List<NewsResponse> newsResponses = NewsTestData.getListNewsResponseSearch();

            when(newsRepository.searchBy(NewsTestData.SEARCH_VALUE, 1,
                    NewsTestData.SEARCH_FIELDS_ARRAY))
                    .thenReturn(news);
            when(newsMapper.newsToResponse(news.get(0)))
                    .thenReturn(newsResponses.get(0));
            when(newsMapper.newsToResponse(news.get(1)))
                    .thenReturn(newsResponses.get(1));

            //when
            List<NewsResponse> actualNews = newsService.searchNews(
                    NewsTestData.SEARCH_VALUE,
                    List.of(),
                    1
            );

            //then
            assertEquals(newsResponses.size(), actualNews.size());
            verify(newsRepository, times(1))
                    .searchBy(NewsTestData.SEARCH_VALUE, 1,
                            NewsTestData.SEARCH_FIELDS_ARRAY);
            verify(newsMapper, times(2)).newsToResponse(any());
        }


        @Test
        void shouldNotGetCommentsWithFullTextSearch_whenFieldsIsNotValid() {
            // given, when, then
            assertThrows(
                    NoSuchSearchFieldException.class,
                    () -> newsService.searchNews(
                            NewsTestData.SEARCH_VALUE,
                            NewsTestData.SEARCH_NOT_VALID_FIELDS, 1));
        }

    }
}