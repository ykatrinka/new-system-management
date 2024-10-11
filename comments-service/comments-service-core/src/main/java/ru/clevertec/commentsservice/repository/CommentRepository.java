package ru.clevertec.commentsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.commentsservice.entity.Comment;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Репозиторий для работы с базой данных.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
