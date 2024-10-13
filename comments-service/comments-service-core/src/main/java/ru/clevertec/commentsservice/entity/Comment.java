package ru.clevertec.commentsservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Katerina
 * @version 1.0.0
 * <p>
 * Сущность "Комментарий".
 */
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    /**
     * Идентификатор.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * Время создания.
     */
    @Column
    @CreationTimestamp
    private LocalDateTime time;

    /**
     * Текст комментария.
     */
    @FullTextField
    @Column
    private String text;

    /**
     * Пользователь.
     */
    @FullTextField
    @Column
    private String username;

    /**
     * Идентификатор новости.
     */
    @Column(name = "news_id")
    private Long newsId;

}
