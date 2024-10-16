package ru.clevertec.newsservice.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Katrina
 * @version 1.0.0
 * <p>
 * Конфигурация Redis Cache.
 * Включает кеширование.
 * Используется для профиля prod.
 */
@Configuration
@Profile("prod")
@EnableCaching
public class RedisCacheConfig {

}
