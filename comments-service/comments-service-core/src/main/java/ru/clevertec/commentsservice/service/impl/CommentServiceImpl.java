package ru.clevertec.commentsservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.commentsservice.dto.request.CommentRequest;
import ru.clevertec.commentsservice.dto.response.CommentResponse;
import ru.clevertec.commentsservice.entity.Comment;
import ru.clevertec.commentsservice.exception.CommentNotFoundException;
import ru.clevertec.commentsservice.mapper.CommentMapper;
import ru.clevertec.commentsservice.repository.CommentRepository;
import ru.clevertec.commentsservice.service.CommentService;
import ru.clevertec.commentsservice.util.Constants;

import java.util.List;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Сервис, реализующий работу с комментариями.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    /**
     * Класс репозитория для работы с базой данных.
     */
    private final CommentRepository commentRepository;
    /**
     * Mapper для конвертации сущностей комментария.
     */
    private final CommentMapper commentMapper;

    /**
     * Метод для добавления нового комментария в базу данных.
     *
     * @param commentRequest Полученные данные для создания комментария.
     * @return CommentResponse
     * Возвращает созданную сущность с новым сгенерированным id.
     */
    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        Comment comment = commentMapper.requestToComment(commentRequest);
        Comment savedComment = commentRepository.save(comment);

        return commentMapper.commentToResponse(savedComment);
    }

    /**
     * Метод для получения списка комментариев с учетом пагинации из базы данных.
     *
     * @param pageNumber Номер страницы.
     * @return Список комментариев из базы данных.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments(int pageNumber) {
        PageRequest pageable = PageRequest.of(pageNumber, Constants.COMMENTS_PAGE_SIZE);
        Page<Comment> pageComments = commentRepository.findAll(pageable);

        return pageComments.getContent().stream()
                .map(commentMapper::commentToResponse)
                .toList();
    }


    /**
     * Метод для получения комментария по идентификатору из базы данных.
     *
     * @param commentsId идентификатор комментария для получения.
     * @return Найденный комментарий из базы данных.
     */
    @Override
    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentsId) {
        return commentRepository.findById(commentsId)
                .map(commentMapper::commentToResponse)
                .orElseThrow(
                        () -> CommentNotFoundException.getById(commentsId)
                );
    }

    /**
     * Метод для обновления комментария в базе данных.
     *
     * @param commentRequest Полученные данные для обновления комментария.
     * @param commentId      идентификатор обновляемого комментария.
     * @return CommentResponse
     * Возвращает обновленная сущность.
     */
    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .map(updComment -> commentMapper.updateFromRequest(commentId, commentRequest))
                .orElseThrow(
                        () -> CommentNotFoundException.getById(commentId)
                );
        Comment updatedComment = commentRepository.save(comment);

        return commentMapper.commentToResponse(updatedComment);
    }

    /**
     * Метод для удаления комментария по id из базы данных.
     *
     * @param commentId идентификатор удаляемого комментария.
     */
    @Override
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(commentRepository::delete);
    }

}
