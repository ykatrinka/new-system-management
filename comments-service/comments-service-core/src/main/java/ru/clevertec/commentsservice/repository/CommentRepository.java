package ru.clevertec.commentsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.clevertec.commentsservice.entity.Comment;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Репозиторий для работы с базой данных.
 */
@Repository
public interface CommentRepository extends SearchRepository<Comment, Long> {
    /**
     * Удаляет все комментарии по идентификатору новости.
     *
     * @param newsId Идентификатор новости.
     */
    void deleteAllByNewsId(Long newsId);

    /**
     * Получает список комментариев
     * по идентификатору новости (с учетом пагинации).
     *
     * @param newsId   идентификатор новости.
     * @param pageable данные о странице.
     * @return Список комментариев по указанной странице.
     */
    Page<Comment> findByNewsId(Long newsId, Pageable pageable);
}
