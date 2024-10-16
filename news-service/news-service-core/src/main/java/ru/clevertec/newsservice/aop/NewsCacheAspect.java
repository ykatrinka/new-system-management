package ru.clevertec.newsservice.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.newsservice.cache.CustomCache;
import ru.clevertec.newsservice.entity.News;
import ru.clevertec.newsservice.util.LoggingMessage;

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
public class NewsCacheAspect {

    private final CustomCache<Long, News> cache;

    /**
     * Методы сохранения в базу данных.
     */
    @Pointcut("execution(* ru.clevertec.newsservice.repository.NewsRepository.save(..))")
    public void saveNewsMethod() {
    }

    /**
     * Методы удаления из базы данных.
     */
    @Pointcut("execution(* ru.clevertec.newsservice.repository.NewsRepository.delete(..))")
    public void deleteNewsMethod() {
    }

    /**
     * Методы получения комментариев.
     */
    @Pointcut("execution(* ru.clevertec.newsservice.repository.NewsRepository.findById(..))")
    public void findByIdNewsMethod() {
    }

    /**
     * При сохранении новость добавляется в кэш.
     *
     * @param joinPoint Данные метода.
     * @return Сохранённая новость.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("saveNewsMethod()")
    public Object aroundSaveNewsMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        News news = (News) joinPoint.getArgs()[0];
        News savedNews = (News) joinPoint.proceed();

        if (savedNews != null && savedNews.getId() != null) {
            cache.put(news.getId(), news);
            log.info(LoggingMessage.NEWS_ADD_TO_CACHE, savedNews.getId());
        }
        return savedNews;
    }

    /**
     * При удалении новость удаляется из кэша.
     *
     * @param joinPoint Данные метода.
     * @return Идентификатор новости.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("deleteNewsMethod()")
    public Object aroundDeleteNewsMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long newsId = ((News) joinPoint.getArgs()[0]).getId();
        joinPoint.proceed();
        cache.delete(newsId);
        log.info(LoggingMessage.NEWS_REMOVE_FROM_CACHE, newsId);
        return newsId;
    }


    /**
     * При получении новость берется из кэша.
     * При ее отсутствии - из БД, после чего добавляется в кэш.
     *
     * @param joinPoint Данные метода.
     * @return Найденная новость.
     * @throws Throwable Возможный выброс исключения при выполнении метода.
     */
    @Around("findByIdNewsMethod()")
    public Object aroundFindByIdNewsMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Long newsId = (Long) joinPoint.getArgs()[0];
        Optional<News> foundNews = cache.get(newsId);
        foundNews.ifPresent(news ->
                log.info(LoggingMessage.NEWS_GET_FROM_CACHE, newsId));

        if (foundNews.isEmpty()) {
            foundNews = (Optional<News>) joinPoint.proceed();
            foundNews.ifPresent(news -> cache.put(newsId, news));
            log.info(LoggingMessage.NEWS_GET_FROM_CACHE, newsId);
        }

        return foundNews;
    }
}
