package ru.clevertec.newsservice.repository;

import org.springframework.stereotype.Repository;
import ru.clevertec.newsservice.entity.News;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Репозиторий для работы с таблицей news базы данных.
 */
@Repository
public interface NewsRepository extends SearchRepository<News, Long> {
}
