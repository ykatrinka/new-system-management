package ru.clevertec.commentsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.entity.Comment;

/**
 * @author Katerina
 * @version 1.0.0
 * Mapper для конвертации сущностей связанных с комментарием.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    /**
     * Метод конвертирует данные типа CommentRequest в Comment.
     * Поле id - пропускается.
     *
     * @param commentRequest объект для конвертации
     * @return объект типа Comment.
     */
    @Mapping(target = "id", ignore = true)
    Comment requestToComment(CommentRequest commentRequest);

    /**
     * Метод конвертирует данные типа Comment в CommentResponse.
     *
     * @param comment объект для конвертации
     * @return объект типа CommentResponse.
     */
    CommentResponse commentToResponse(Comment comment);

    /**
     * Метод конвертирует данные типа CommentRequest в Comment.
     *
     * @param commentRequest объект для конвертации.
     * @param commentId      Устанавливаемый идентификатор.
     * @return объект типа Comment.
     */
    @Mapping(target = "id", source = "commentId")
    Comment updateFromRequest(Long commentId, CommentRequest commentRequest);
}
