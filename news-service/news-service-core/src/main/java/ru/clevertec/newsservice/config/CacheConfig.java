package ru.clevertec.newsservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.clevertec.newsservice.cache.CustomCache;
import ru.clevertec.newsservice.cache.factory.CacheType;
import ru.clevertec.newsservice.cache.factory.impl.CacheFactoryImpl;
import ru.clevertec.newsservice.entity.News;
import ru.clevertec.newsservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Конфигурация кеширования.
 */
@Profile("dev")
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
    public CustomCache<Long, News> cacheBean() {
        return new CacheFactoryImpl<Long, News>().getInstance(cacheType);
    }
}
