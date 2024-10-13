package ru.clevertec.commentsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.commentsservice.cache.CustomCache;
import ru.clevertec.commentsservice.cache.factory.CacheType;
import ru.clevertec.commentsservice.cache.factory.impl.CacheFactoryImpl;
import ru.clevertec.commentsservice.entity.Comment;
import ru.clevertec.commentsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Конфигурация кеширования.
 */
@Configuration
public class CacheConfig {

    /**
     * Тип кеширования.
     */
    @Value(Constants.CACHE_TYPE)
    private CacheType cacheType;

    /**
     * Bean кэша.
     *
     * @return CustomCache.
     */
    @Bean
    public CustomCache<Long, Comment> cacheBean() {
        return new CacheFactoryImpl<Long, Comment>().getInstance(cacheType);
    }
}
