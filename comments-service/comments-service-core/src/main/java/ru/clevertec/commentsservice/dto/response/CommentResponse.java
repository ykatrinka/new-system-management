package ru.clevertec.commentsservice.dto.response;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @param id       идентификатор комментария
 * @param username Имя пользователя.
 * @param newsId   Идентификатор новости.
 * @param time     Время создания комментария (генерируется автоматически).
 * @param text     Текст комментария.
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность для передачи данных комментария пользователю.
 */
@Builder
public record CommentResponse(

        Long id,
        LocalDateTime time,
        String text,
        String username,
        Long newsId
) implements Serializable {

}
