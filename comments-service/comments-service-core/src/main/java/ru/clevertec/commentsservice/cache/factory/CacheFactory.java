package ru.clevertec.commentsservice.cache.factory;


import ru.clevertec.commentsservice.cache.CustomCache;

/**
 * @param <K> тип идентификатора.
 * @param <V> тип объекта.
 *            Фабрика для получения реализации кэша.
 * @author Katerina
 * @version 1.0.0
 */
public interface CacheFactory<K, V> {
    /**
     * Получает реализацию кэша.
     *
     * @param type тип кеширования.
     * @return CustomCache.
     */
    CustomCache<K, V> getInstance(CacheType type);
}
