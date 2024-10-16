package ru.clevertec.commentsservice.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.commentsservice.cache.CustomCache;
import ru.clevertec.commentsservice.entity.Comment;
import ru.clevertec.commentsservice.util.LoggingMessage;

import java.util.Optional;

/**
 * @author Katerina
 * @version 1.0.0
 * Аспект для работы с кэшем.
 */
@Slf4j
@Profile("dev")
@Component
@Aspect
@RequiredArgsConstructor
public class CommentsCacheAspect {

    /**
     * Кэш.
     */
    private final CustomCache<Long, Comment> cache;

    /**
     * Методы сохранения в базу данных.
     */
    @Pointcut("execution(* ru.clevertec.commentsservice.repository.CommentRepository.save(..))")
    public void saveCommentMethod() {
    }

    /**
     * Методы удаления из базы данных.
     */
    @Pointcut("execution(* ru.clevertec.commentsservice.repository.CommentRepository.delete(..))")
    public void deleteCommentMethod() {
    }

    /**
     * Методы получения комментариев.
     */
    @Pointcut("execution(* ru.clevertec.commentsservice.repository.CommentRepository.findById(..))")
    public void findByIdCommentMethod() {
    }

    /**
     * При сохранении комментарий добавляется в кэш.
     *
     * @param joinPoint Данные метода.
     * @return Сохранённый комментарий.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("saveCommentMethod()")
    public Object aroundSaveCommentMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        Comment savedComment = (Comment) joinPoint.proceed();

        if (savedComment != null && savedComment.getId() != null) {
            cache.put(comment.getId(), comment);
            log.info(LoggingMessage.COMMENT_ADD_TO_CACHE, savedComment.getId());
        }
        return savedComment;
    }

    /**
     * При удалении комментария он удаляется из кэша.
     *
     * @param joinPoint Данные метода.
     * @return Идентификатор комментария.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("deleteCommentMethod()")
    public Object aroundDeleteCommentMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long commentId = ((Comment) joinPoint.getArgs()[0]).getId();
        joinPoint.proceed();
        cache.delete(commentId);
        log.info(LoggingMessage.COMMENT_REMOVE_FROM_CACHE, commentId);
        return commentId;
    }


    /**
     * При получении комментарий берется из кэша.
     * При его отсутствии из БД, после чего добавляется в кэш.
     *
     * @param joinPoint Данные метода.
     * @return Найденный комментарий.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("findByIdCommentMethod()")
    public Object aroundFindByIdCommentMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long commentId = (Long) joinPoint.getArgs()[0];
        Optional<Comment> foundComment = cache.get(commentId);
        foundComment.ifPresent(comment ->
                log.info(LoggingMessage.COMMENT_GET_FROM_CACHE, commentId));

        if (foundComment.isEmpty()) {
            foundComment = (Optional<Comment>) joinPoint.proceed();
            foundComment.ifPresent(comment -> cache.put(commentId, comment));
            log.info(LoggingMessage.COMMENT_GET_FROM_CACHE, commentId);
        }

        return foundComment;
    }
}
