package ru.clevertec.newsservice.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import ru.clevertec.newsservice.repository.SearchRepository;

import java.io.Serializable;
import java.util.List;

/**
 * Репозиторий для полнотекстового поиска.
 *
 * @param <T> Тип сущности для полнотекстового поиска.
 * @param <K> Тип идентификатора (id).
 * @author Katerina
 * @version 1.0.0
 */
@Transactional
public class SearchRepositoryImpl<T, K extends Serializable>
        extends SimpleJpaRepository<T, K>
        implements SearchRepository<T, K> {

    /**
     * EntityManager для работы с базой данных.
     */
    private final EntityManager entityManager;

    /**
     * Конструктор для инициализации с указанием класса сущности.
     *
     * @param domainClass   Тип сущности.
     * @param entityManager объект типа EntityManager.
     */
    public SearchRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    public SearchRepositoryImpl(
            JpaEntityInformation<T, K> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /**
     * Метод для получения списка элементов, соответствующих
     * параметрам полнотекстового поиска.
     *
     * @param text   Текст для поиска.
     * @param limit  Количество элементов.
     * @param fields Перечень полей для поиска.
     *               Если пустой, то выбираются поля по умолчанию
     *               (с аннотацией FullTextSearch).
     * @return Список элементов, соответствующих
     * * параметрам полнотекстового поиска.
     */
    @Override
    public List<T> searchBy(String text, int limit, String... fields) {

        SearchResult<T> result = getSearchResult(text, limit, fields);

        return result.hits();
    }

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
    private SearchResult<T> getSearchResult(String text, int limit, String[] fields) {
        SearchSession searchSession = Search.session(entityManager);

        return searchSession
                .search(getDomainClass())
                .where(f -> f.match()
                        .fields(fields)
                        .matching("*" + text + "*")
                        .fuzzy(2)
                )
                .fetch(limit);
    }
}