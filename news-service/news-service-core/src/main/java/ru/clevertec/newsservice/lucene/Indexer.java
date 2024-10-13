package ru.clevertec.newsservice.lucene;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.newsservice.exception.NewsIndexException;
import ru.clevertec.newsservice.util.Constants;

/**
 * Listner для работы с кэшем.
 */
@Transactional
@Component
@RequiredArgsConstructor
public class Indexer implements ApplicationListener<ApplicationStartedEvent> {

    private final EntityManager entityManager;

    private static final int THREAD_NUMBER = 4;

    public void indexPersistedData(String indexClassName) throws NewsIndexException {

        try {
            SearchSession searchSession = Search.session(entityManager);

            Class<?> classToIndex = Class.forName(indexClassName);
            MassIndexer indexer =
                    searchSession
                            .massIndexer(classToIndex)
                            .threadsToLoadObjects(THREAD_NUMBER);

            indexer.startAndWait();
        } catch (ClassNotFoundException e) {
            throw NewsIndexException.getInstance(
                    String.format(Constants.ERROR_INVALID_CLASS, indexClassName));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw NewsIndexException.getInstance(Constants.ERROR_INDEX_INTERRUPTED);
        }
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        this.indexPersistedData(Constants.PATH_NEWS);
    }
}