package ru.clevertec.commentsservice.service.impl;

import feign.FeignException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.dto.response.NewsResponse;
import ru.clevertec.commentsservice.entity.Comment;
import ru.clevertec.commentsservice.exception.CommentNotFoundException;
import ru.clevertec.commentsservice.exception.FeignServerErrorException;
import ru.clevertec.commentsservice.exception.NewsNotFoundException;
import ru.clevertec.commentsservice.exception.NoSuchSearchFieldException;
import ru.clevertec.commentsservice.feignclient.NewsFeignClient;
import ru.clevertec.commentsservice.mapper.CommentMapper;
import ru.clevertec.commentsservice.repository.CommentRepository;
import util.CommentTestData;

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
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private NewsFeignClient newsFeignClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Nested
    class Create {
        @Test
        void shouldCreateComment() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();
            CommentResponse commentResponse = CommentTestData.getCommentResponseForCreate();
            ResponseEntity<NewsResponse> newsResponse = CommentTestData.getNewsResponse();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenReturn(newsResponse);
            when(commentMapper.requestToComment(commentRequest))
                    .thenReturn(comment);
            when(commentRepository.save(comment))
                    .thenReturn(createdComment);
            when(commentMapper.commentToResponse(createdComment))
                    .thenReturn(commentResponse);

            //when
            CommentResponse actualCommentResponse = commentService.createComment(commentRequest);

            //then
            assertAll(
                    () -> assertNotNull(actualCommentResponse),
                    () -> assertNotNull(actualCommentResponse.id()),
                    () -> assertNotNull(actualCommentResponse.time()),
                    () -> assertEquals(commentResponse.newsId(), actualCommentResponse.newsId()),
                    () -> assertEquals(commentResponse.username(), actualCommentResponse.username()),
                    () -> assertEquals(commentResponse.text(), actualCommentResponse.text())
            );

            verify(commentMapper, times(1)).requestToComment(commentRequest);
            verify(commentRepository, times(1)).save(comment);
            verify(commentMapper, times(1)).commentToResponse(createdComment);
        }

        @Test
        void shouldNotCreateComment_whenNewsNotFound() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenThrow(NewsNotFoundException.class);

            //when
            assertThrows(NewsNotFoundException.class,
                    () -> commentService.createComment(commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
            verify(commentRepository, times(0)).save(comment);
            verify(commentMapper, times(0)).commentToResponse(createdComment);
        }

        @Test
        void shouldNotCreateComment_whenFeignReturn500() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenReturn(CommentTestData.getNewsResponseServerError());

            //when
            assertThrows(FeignServerErrorException.class,
                    () -> commentService.createComment(commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
            verify(commentRepository, times(0)).save(comment);
            verify(commentMapper, times(0)).commentToResponse(createdComment);
        }

        @Test
        void shouldNotCreateComment_whenFeignResponseBodyIsNull() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenReturn(CommentTestData.getNewsResponseNotFound());

            //when
            assertThrows(NewsNotFoundException.class,
                    () -> commentService.createComment(commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
            verify(commentRepository, times(0)).save(comment);
            verify(commentMapper, times(0)).commentToResponse(createdComment);
        }


        @Test
        void shouldNotCreateComment_whenThrowFeignExceptionNotFound() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenThrow(FeignException.NotFound.class);

            //when
            assertThrows(NewsNotFoundException.class,
                    () -> commentService.createComment(commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
            verify(commentRepository, times(0)).save(comment);
            verify(commentMapper, times(0)).commentToResponse(createdComment);
        }


        @Test
        void shouldNotCreateComment_whenThrowFeignException() {
            //given
            CommentRequest commentRequest = CommentTestData.getCommentRequestForCreate();
            Comment comment = CommentTestData.getCommentForCreate();
            Comment createdComment = CommentTestData.getFillCommentForCreate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenThrow(FeignException.class);

            //when
            assertThrows(FeignServerErrorException.class,
                    () -> commentService.createComment(commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
            verify(commentRepository, times(0)).save(comment);
            verify(commentMapper, times(0)).commentToResponse(createdComment);
        }

    }


    @Test
    void shouldGetAllComments() {
        //given
        PageRequest pageable = PageRequest.of(
                CommentTestData.PAGE_NUMBER,
                CommentTestData.COMMENTS_PAGE_SIZE);
        Page<Comment> pageComments = CommentTestData.getPageableListComments();
        List<CommentResponse> commentResponses = CommentTestData.getListCommentsResponse();

        when(commentRepository.findAll(pageable)).thenReturn(pageComments);
        when(commentMapper.commentToResponse(pageComments.getContent().get(0)))
                .thenReturn(commentResponses.get(0));
        when(commentMapper.commentToResponse(pageComments.getContent().get(1)))
                .thenReturn(commentResponses.get(1));
        when(commentMapper.commentToResponse(pageComments.getContent().get(2)))
                .thenReturn(commentResponses.get(2));
        when(commentMapper.commentToResponse(pageComments.getContent().get(3)))
                .thenReturn(commentResponses.get(3));

        //when
        List<CommentResponse> actualComments = commentService
                .getAllComments(CommentTestData.PAGE_NUMBER);

        //then
        assertEquals(commentResponses.size(), actualComments.size());

        verify(commentRepository, times(1)).findAll(pageable);
        verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(0));
        verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(1));
        verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(2));
        verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(3));
    }

    @Nested
    class getById {

        @Test
        void shouldGetCommentById() {
            //given
            Long commentId = CommentTestData.COMMENT_ID_FOR_GET;
            Comment comment = CommentTestData.getFillCommentForGetById();
            CommentResponse commentResponse = CommentTestData.getFillCommentResponseForGetById();

            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
            when(commentMapper.commentToResponse(comment)).thenReturn(commentResponse);

            //when
            CommentResponse actualComment = commentService.getCommentById(commentId);

            //then
            assertAll(
                    () -> assertNotNull(actualComment),
                    () -> assertEquals(commentId, actualComment.id()),
                    () -> assertEquals(commentResponse.time(), actualComment.time()),
                    () -> assertEquals(commentResponse.username(), actualComment.username()),
                    () -> assertEquals(commentResponse.newsId(), actualComment.newsId()),
                    () -> assertEquals(commentResponse.text(), actualComment.text())
            );

            verify(commentRepository, times(1)).findById(commentId);
            verify(commentMapper, times(1)).commentToResponse(comment);
        }

        @Test
        void shouldNotGetCommentById_whenCommentByIdNotFound() {
            //given
            Long commentId = CommentTestData.COMMENT_ID_FOR_GET;
            CommentNotFoundException commentNotFoundException = CommentNotFoundException.getById(commentId);
            when(commentRepository.findById(commentId))
                    .thenThrow(commentNotFoundException);

            //when, then
            assertThrows(CommentNotFoundException.class,
                    () -> commentService.getCommentById(commentId));

            verify(commentRepository, times(1))
                    .findById(commentId);
            verify(commentMapper, times(0))
                    .commentToResponse(any());
        }
    }

    @Nested
    class update {
        @Test
        void shouldUpdateComment() {
            //given
            long commentId = CommentTestData.COMMENT_ID_FOR_UPDATE;
            CommentRequest commentRequest = CommentTestData.getCommentRequestForUpdate();
            Optional<Comment> comment = CommentTestData.getCommentForUpdate();
            Comment updatedComment = CommentTestData.getUpdatedCommentForUpdate();
            CommentResponse commentResponse = CommentTestData.getCommentResponseForUpdate();
            ResponseEntity<NewsResponse> newsResponse = CommentTestData.getNewsResponse();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenReturn(newsResponse);
            when(commentRepository.findById(commentId))
                    .thenReturn(comment);
            when(commentMapper.updateFromRequest(commentId, commentRequest))
                    .thenReturn(updatedComment);
            when(commentRepository.save(updatedComment)).thenReturn(updatedComment);
            when(commentMapper.commentToResponse(updatedComment)).thenReturn(commentResponse);

            //when
            CommentResponse actualComment = commentService.updateComment(commentId, commentRequest);

            //then
            assertAll(
                    () -> assertNotNull(actualComment),
                    () -> assertEquals(commentResponse.id(), actualComment.id()),
                    () -> assertEquals(commentResponse.time(), actualComment.time()),
                    () -> assertEquals(commentResponse.newsId(), actualComment.newsId()),
                    () -> assertEquals(commentResponse.username(), actualComment.username()),
                    () -> assertEquals(commentResponse.text(), actualComment.text())
            );

            verify(commentRepository, times(1))
                    .findById(commentId);
            verify(commentMapper, times(1))
                    .updateFromRequest(commentId, commentRequest);
            verify(commentRepository, times(1))
                    .save(updatedComment);
            verify(commentMapper, times(1))
                    .commentToResponse(updatedComment);
        }

        @Test
        void shouldNotUpdateComment_whenCommentNotFound() {
            //given
            long commentId = CommentTestData.COMMENT_ID_FOR_UPDATE;
            CommentRequest commentRequest = CommentTestData.getCommentRequestForUpdate();
            ResponseEntity<NewsResponse> newsResponse = CommentTestData.getNewsResponse();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenReturn(newsResponse);
            when(commentRepository.findById(commentId))
                    .thenReturn(Optional.empty());


            //when, then
            assertThrows(CommentNotFoundException.class,
                    () -> commentService.updateComment(commentId, commentRequest));

            verify(commentRepository, times(1))
                    .findById(commentId);
        }

        @Test
        void shouldNotUpdateComment_whenNewsNotFound() {
            //given
            long commentId = CommentTestData.COMMENT_ID_FOR_UPDATE;
            CommentRequest commentRequest = CommentTestData.getCommentRequestForUpdate();

            when(newsFeignClient.getNewsById(commentRequest.newsId()))
                    .thenThrow(NewsNotFoundException.class);

            //when
            assertThrows(NewsNotFoundException.class,
                    () -> commentService.updateComment(commentId, commentRequest));

            //then
            verify(commentMapper, times(0)).requestToComment(commentRequest);
        }


    }

    @Nested
    class delete {

        @Test
        void shouldDeleteComment() {
            //given
            long commentId = CommentTestData.COMMENT_ID_FOR_DELETE;
            Comment comment = CommentTestData.getFillCommentForGetById();

            //when
            when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

            //then
            commentService.deleteComment(commentId);
            verify(commentRepository, times(1)).findById(commentId);
            verify(commentRepository, times(1)).delete(comment);
        }

    }

    @Nested
    class search {

        @Test
        void shouldGetCommentsWithFullTextSearch() {
            //given
            List<Comment> comments = CommentTestData.getListCommentsSearch();
            List<CommentResponse> commentResponses = CommentTestData.getListCommentsResponseSearch();

            when(commentRepository.searchBy(CommentTestData.SEARCH_VALUE, 1,
                    CommentTestData.SEARCH_FIELDS_ARRAY))
                    .thenReturn(comments);
            when(commentMapper.commentToResponse(comments.get(0)))
                    .thenReturn(commentResponses.get(0));
            when(commentMapper.commentToResponse(comments.get(1)))
                    .thenReturn(commentResponses.get(1));

            //when
            List<CommentResponse> actualComments = commentService.searchComments(
                    CommentTestData.SEARCH_VALUE,
                    CommentTestData.SEARCH_FIELDS,
                    1
            );

            //then
            assertEquals(commentResponses.size(), actualComments.size());
            verify(commentRepository, times(1))
                    .searchBy(CommentTestData.SEARCH_VALUE, 1,
                            CommentTestData.SEARCH_FIELDS_ARRAY);
            verify(commentMapper, times(2)).commentToResponse(any());
        }


        @Test
        void shouldGetCommentsWithFullTextSearch_emptyFields() {
            //given
            List<Comment> comments = CommentTestData.getListCommentsSearch();
            List<CommentResponse> commentResponses = CommentTestData.getListCommentsResponseSearch();

            when(commentRepository.searchBy(CommentTestData.SEARCH_VALUE, 1,
                    CommentTestData.SEARCH_FIELDS_ARRAY))
                    .thenReturn(comments);
            when(commentMapper.commentToResponse(comments.get(0)))
                    .thenReturn(commentResponses.get(0));
            when(commentMapper.commentToResponse(comments.get(1)))
                    .thenReturn(commentResponses.get(1));

            //when
            List<CommentResponse> actualComments = commentService.searchComments(
                    CommentTestData.SEARCH_VALUE,
                    List.of(),
                    1
            );

            //then
            assertEquals(commentResponses.size(), actualComments.size());
            verify(commentRepository, times(1))
                    .searchBy(CommentTestData.SEARCH_VALUE, 1,
                            CommentTestData.SEARCH_FIELDS_ARRAY);
            verify(commentMapper, times(2)).commentToResponse(any());
        }

        @Test
        void shouldNotGetCommentsWithFullTextSearch_whenFieldsIsNotValid() {
            // given, when, then
            assertThrows(
                    NoSuchSearchFieldException.class,
                    () -> commentService.searchComments(
                            CommentTestData.SEARCH_VALUE,
                            CommentTestData.SEARCH_NOT_VALID_FIELDS, 1));
        }

    }

    @Test
    void shouldDeleteCommentsByNewsId() {
        //given
        long newsId = CommentTestData.NEWS_ID;

        //when, then
        commentService.deleteCommentsByNewsId(newsId);
        verify(commentRepository, times(1)).deleteAllByNewsId(newsId);
    }


    @Nested
    class getCommentsByNewsId {

        @Test
        void shouldGetCommentByNewsId() {
            //given
            PageRequest pageable = PageRequest.of(
                    CommentTestData.PAGE_NUMBER,
                    CommentTestData.COMMENTS_PAGE_SIZE);
            long newsId = CommentTestData.NEWS_ID;

            Page<Comment> pageComments = CommentTestData.getPageableListComments();
            List<CommentResponse> commentResponses = CommentTestData.getListCommentsResponse();

            when(newsFeignClient.getNewsById(newsId))
                    .thenReturn(CommentTestData.getNewsResponse());
            when(commentRepository.findByNewsId(newsId, pageable))
                    .thenReturn(pageComments);
            when(commentMapper.commentToResponse(pageComments.getContent().get(0)))
                    .thenReturn(commentResponses.get(0));
            when(commentMapper.commentToResponse(pageComments.getContent().get(1)))
                    .thenReturn(commentResponses.get(1));
            when(commentMapper.commentToResponse(pageComments.getContent().get(2)))
                    .thenReturn(commentResponses.get(2));
            when(commentMapper.commentToResponse(pageComments.getContent().get(3)))
                    .thenReturn(commentResponses.get(3));

            //when
            List<CommentResponse> actualComments = commentService
                    .getCommentsByNewsId(newsId, CommentTestData.PAGE_NUMBER);

            //then
            assertEquals(commentResponses.size(), actualComments.size());

            verify(commentRepository, times(1)).findByNewsId(newsId, pageable);
            verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(0));
            verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(1));
            verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(2));
            verify(commentMapper, times(1)).commentToResponse(pageComments.getContent().get(3));
        }

        @Test
        void shouldNotGetAllCommentByNewsId_whenNewsIdIsNotExists() {
            //given
            Long newsId = CommentTestData.NEWS_ID;

            when(newsFeignClient.getNewsById(newsId))
                    .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

            //when, then
            assertThrows(NewsNotFoundException.class, () ->
                    commentService.getCommentsByNewsId(newsId, CommentTestData.PAGE_NUMBER));

            verify(newsFeignClient, times(1))
                    .getNewsById(newsId);
        }
    }
}