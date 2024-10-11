package ru.clevertec.commentsservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.exception.CommentNotFoundException;
import ru.clevertec.commentsservice.service.CommentService;
import ru.clevertec.commentsservice.util.CommentTestData;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateComment() throws Exception {
        //given
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        CommentResponse commentResponse = CommentTestData.getCommentResponse();
        String commentJson = objectMapper.writeValueAsString(commentRequest);

        when(commentService.createComment(commentRequest))
                .thenReturn(commentResponse);

        //when, then
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(commentResponse.id()))
                .andExpect(jsonPath("$.newsId").value(commentResponse.newsId()))
                .andExpect(jsonPath("$.username").value(commentResponse.username()))
                .andExpect(jsonPath("$.text").value(commentResponse.text())
                );

        verify(commentService, times(1)).createComment(commentRequest);

    }


    @Test
    void shouldGetAllComments() throws Exception {
        //given
        List<CommentResponse> comments = CommentTestData.getListWithTwoCommentResponse();
        when(commentService.getAllComments(0))
                .thenReturn(comments);

        //when, then
        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(commentService, times(0))
                .getAllComments(1);
    }

    @Nested
    class GetById {

        @Test
        void shouldGetCommentById() throws Exception {
            //given
            Long commentId = CommentTestData.COMMENT_ID;
            CommentResponse commentResponse = CommentTestData.getCommentResponse();

            when(commentService.getCommentById(commentId))
                    .thenReturn(commentResponse);

            //when, then
            mockMvc.perform(get("/comments/{commentId}", commentId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(IsNull.notNullValue()))
                    .andExpect(jsonPath("$.id").value(commentResponse.id()))
                    .andExpect(jsonPath("$.newsId").value(commentResponse.newsId()))
                    .andExpect(jsonPath("$.username").value(commentResponse.username()))
                    .andExpect(jsonPath("$.text").value(commentResponse.text())
                    );

            verify(commentService, times(1))
                    .getCommentById(commentId);
        }


        @Test
        void shouldNotGetCommentById_whenCommentNotFound() throws Exception {
            //given
            Long commentId = CommentTestData.COMMENT_ID;

            when(commentService.getCommentById(commentId))
                    .thenThrow(CommentNotFoundException.class);

            //when, then
            mockMvc.perform(get("/comments/{commentId}", commentId))
                    .andExpect(status().isNotFound());

            verify(commentService, times(1))
                    .getCommentById(commentId);
        }


    }


    @Test
    void shouldUpdateCommentById() throws Exception {
        //given
        Long commentId = CommentTestData.COMMENT_ID;
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        CommentResponse updatedComment = CommentTestData.getCommentResponse();
        String commentJson = objectMapper.writeValueAsString(commentRequest);

        when(commentService.updateComment(commentId, commentRequest))
                .thenReturn(updatedComment);

        //when, then
        mockMvc.perform(put("/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedComment.id()))
                .andExpect(jsonPath("$.newsId").value(updatedComment.newsId()))
                .andExpect(jsonPath("$.username").value(updatedComment.username()))
                .andExpect(jsonPath("$.text").value(updatedComment.text())
                )
        ;

        verify(commentService, times(1))
                .updateComment(commentId, commentRequest);

    }

    @Test
    void shouldNotUpdateCommentById_whenCommentNotFound() throws Exception {
        //given
        Long commentId = CommentTestData.COMMENT_ID;
        CommentRequest commentRequest = CommentTestData.getCommentRequest();
        String commentJson = objectMapper.writeValueAsString(commentRequest);

        when(commentService.updateComment(commentId, commentRequest))
                .thenThrow(CommentNotFoundException.class);

        //when, then
        mockMvc.perform(put("/comments/{commentId}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isNotFound());

        verify(commentService, times(1))
                .updateComment(commentId, commentRequest);
    }


}