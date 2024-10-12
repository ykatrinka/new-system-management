package ru.clevertec.newsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Репозиторий полнотекстового поиска.
 *
 * @param <T> Тип сущности для полнотекстового поиска.
 * @param <K> Тип идентификатора (id).
 * @author Katerina
 * @version 1.0.0
 */
@NoRepositoryBean
public interface SearchRepository<T, K extends Serializable>
        extends JpaRepository<T, K> {

    /**
     * Метод обращается к базе данных для получения списка элементов,
     * соответствующих параметрам полнотекстового поиска
     * по указанным полям.
     *
     * @param text   Текст для поиска.
     * @param limit  Количество элементов.
     * @param fields Перечень полей для поиска.
     *               Если пустой, то выбираются поля по умолчанию
     *               (с аннотацией FullTextSearch).
     * @return Список элементов, соответствующих
     * * параметрам полнотекстового поиска.
     */
    List<T> searchBy(String text, int limit, String... fields);
}