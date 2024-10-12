package ru.clevertec.commentsservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.service.CommentService;

import java.util.List;

/**
 * @author Katerina
 * @version 1.0
 * <p>
 * Контроллер для работы с комментариями.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    /**
     * Сервисный класс для работы с комментариями.
     */
    private final CommentService commentService;

    /**
     * Endpoint для создания нового комментария.
     *
     * @param commentRequest Данные из запроса на создание комментария
     * @return CommentResponse
     * Созданный комментарий с новым сгенерированным id.
     */
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.createComment(commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Endpoint для получения списка комментариев.
     * Используется пагинация.
     * Если не указан номер страницы, то используется первая (по умолчанию)
     *
     * @param pageNumber Номер страницы для получения списка.
     * @return Список комментариев с учетом указанной страницы.
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(
            @RequestParam(
                    name = "pageNumber",
                    defaultValue = "1",
                    required = false) int pageNumber
    ) {
        List<CommentResponse> comments = commentService.getAllComments(pageNumber - 1);
        return new ResponseEntity<>(comments, comments.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK);
    }

    /**
     * Endpoint для получения комментария по id.
     *
     * @param commentId идентификатор получаемого комментария
     * @return CommentResponse
     * Возвращает комментарий с данными.
     */
    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable("commentId") Long commentId) {
        CommentResponse comment = commentService.getCommentById(commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * Endpoint для обновления данных комментария.
     *
     * @param commentId      идентификатор обновляемого комментария
     * @param commentRequest Данные из запроса для обновления комментария
     * @return CommentResponse
     * Обновленный комментарий.
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("commentId") Long commentId,
                                                         @Valid @RequestBody CommentRequest commentRequest) {
        CommentResponse comment = commentService.updateComment(commentId, commentRequest);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * Endpoint для удаления комментария по идентификатору.
     *
     * @param commentId Идентификатор комментария для удаления.
     */
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
    }

    /**
     * Endpoint для полнотекстового поиска комментариев.
     * Возвращает список комментариев, соответствующих параметрам поиска.
     *
     * @param text   Текст для поиска.
     * @param limit  Необязательный параметр.
     *               Количество записей (по умолчанию 15).
     * @param fields Перечень полей для поиска.
     */
    @GetMapping("/search")
    public ResponseEntity<List<CommentResponse>> searchComments(
            @RequestParam(name = "text") String text,
            @RequestParam(
                    name = "limit",
                    defaultValue = "15",
                    required = false) int limit,
            @RequestParam(name = "fields") List<String> fields
    ) {
        return new ResponseEntity<>(commentService.searchComments(text, fields, limit),
                HttpStatus.OK);
    }
}
