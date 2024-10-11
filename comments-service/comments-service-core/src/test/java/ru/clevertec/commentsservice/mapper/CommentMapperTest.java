package ru.clevertec.commentsservice.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.entity.Comment;
import util.CommentTestData;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper commentMapper = new CommentMapperImpl();

    @Test
    void shouldConvertCommentRequestToComment() {
        //given
        CommentRequest commentRequest = CommentTestData.getFillCommentRequest();

        //when
        Comment actualComment = commentMapper.requestToComment(commentRequest);

        //then
        assertAll(
                () -> assertNull(actualComment.getId()),
                () -> assertEquals(commentRequest.time(), actualComment.getTime()),
                () -> assertEquals(commentRequest.username(), actualComment.getUsername()),
                () -> assertEquals(commentRequest.newsId(), actualComment.getNewsId()),
                () -> assertEquals(commentRequest.text(), actualComment.getText())
        );
    }

    @Test
    void shouldConvertCommentRequestToComment_whenRequestIsNull() {
        //given
        CommentRequest commentRequest = null;

        //when
        Comment actualComment = commentMapper.requestToComment(commentRequest);

        //then
        assertAll(
                () -> assertNull(actualComment)
        );
    }

    @Test
    void shouldConvertCommentToCommentResponse() {
        //given
        Comment comment = CommentTestData.getFillCommentWithId();

        //when
        CommentResponse actualCommentResponse = commentMapper.commentToResponse(comment);

        //then
        assertAll(
                () -> assertNotNull(actualCommentResponse),
                () -> assertEquals(comment.getId(), actualCommentResponse.id()),
                () -> assertEquals(comment.getTime(), actualCommentResponse.time()),
                () -> assertEquals(comment.getUsername(), actualCommentResponse.username()),
                () -> assertEquals(comment.getNewsId(), actualCommentResponse.newsId()),
                () -> assertEquals(comment.getText(), actualCommentResponse.text())
        );
    }

    @Test
    void shouldConvertCommentToCommentResponse_whenCommentIsNull() {
        //given
        Comment comment = null;

        //when
        CommentResponse actualCommentResponse = commentMapper.commentToResponse(comment);

        //then
        assertAll(
                () -> assertNull(actualCommentResponse)
        );
    }

    @Test
    void shouldUpdateCommentFromCommentRequest() {
        //given
        CommentRequest commentRequest = CommentTestData.getFillCommentRequest();
        long commentId = CommentTestData.COMMENT_ID_FOR_GET;


        //when
        Comment actualComment = commentMapper.updateFromRequest(commentId, commentRequest);

        //then
        assertAll(
                () -> assertNotNull(actualComment),
                () -> assertEquals(commentId, actualComment.getId()),
                () -> assertEquals(commentRequest.time(), actualComment.getTime()),
                () -> assertEquals(commentRequest.username(), actualComment.getUsername()),
                () -> assertEquals(commentRequest.newsId(), actualComment.getNewsId()),
                () -> assertEquals(commentRequest.text(), actualComment.getText())
        );
    }


    @Test
    void shouldUpdateCommentFromCommentRequest_whenUpdateCommentIsNull() {
        //given
        CommentRequest commentRequest = null;
        Long commentId = null;


        //when
        Comment actualComment = commentMapper.updateFromRequest(commentId, commentRequest);

        //then
        assertAll(
                () -> assertNull(actualComment)
        );
    }

}