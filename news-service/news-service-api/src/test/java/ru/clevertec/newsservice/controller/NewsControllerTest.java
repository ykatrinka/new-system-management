package ru.clevertec.newsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.exception.NewsNotFoundException;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.util.NewsTestData;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@WebMvcTest(NewsController.class)
@AutoConfigureMockMvc
class NewsControllerTest {

    @MockBean
    private NewsService newsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateNews() throws Exception {
        //given
        NewsRequest newsRequest = NewsTestData.getNewsRequest();
        NewsResponse newsResponse = NewsTestData.getNewsResponse();
        String newsJson = objectMapper.writeValueAsString(newsRequest);

        when(newsService.createNews(newsRequest))
                .thenReturn(newsResponse);

        //when, then
        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newsJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newsResponse.id()))
                .andExpect(jsonPath("$.title").value(newsResponse.title()))
                .andExpect(jsonPath("$.text").value(newsResponse.text())
                );

        verify(newsService, times(1)).createNews(newsRequest);

    }

    @Test
    void shouldGetAllNews() throws Exception {
        //given
        List<NewsResponse> news = NewsTestData.getListWithTwoCommentResponse();
        when(newsService.getAllNews(0))
                .thenReturn(news);

        //when, then
        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(newsService, times(0))
                .getAllNews(1);
    }

    @Nested
    class GetById {

        @Test
        void shouldGetNewsById() throws Exception {
            //given
            Long newsId = NewsTestData.NEWS_ID;
            NewsResponse newsResponse = NewsTestData.getNewsResponse();

            when(newsService.getNewsById(newsId))
                    .thenReturn(newsResponse);

            //when, then
            mockMvc.perform(get("/news/{newsId}", newsId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(IsNull.notNullValue()))
                    .andExpect(jsonPath("$.id").value(newsResponse.id()))
                    .andExpect(jsonPath("$.title").value(newsResponse.title()))
                    .andExpect(jsonPath("$.text").value(newsResponse.text())
                    );

            verify(newsService, times(1))
                    .getNewsById(newsId);
        }


        @Test
        void shouldNotGetNewsById_whenNewsNotFound() throws Exception {
            //given
            Long newsId = NewsTestData.NEWS_ID;

            when(newsService.getNewsById(newsId))
                    .thenThrow(NewsNotFoundException.class);

            //when, then
            mockMvc.perform(get("/news/{newsId}", newsId))
                    .andExpect(status().isNotFound());

            verify(newsService, times(1))
                    .getNewsById(newsId);
        }
    }

    @Test
    void shouldUpdateNewsById() throws Exception {
        //given
        Long newsId = NewsTestData.NEWS_ID;
        NewsRequest newsRequest = NewsTestData.getNewsRequest();
        NewsResponse updatedNews = NewsTestData.getNewsResponse();
        String newsJson = objectMapper.writeValueAsString(newsRequest);

        when(newsService.updateNews(newsId, newsRequest))
                .thenReturn(updatedNews);

        //when, then
        mockMvc.perform(put("/news/{newsId}", newsId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newsJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedNews.id()))
                .andExpect(jsonPath("$.title").value(updatedNews.title()))
                .andExpect(jsonPath("$.text").value(updatedNews.text())
                )
        ;

        verify(newsService, times(1))
                .updateNews(newsId, newsRequest);

    }

}