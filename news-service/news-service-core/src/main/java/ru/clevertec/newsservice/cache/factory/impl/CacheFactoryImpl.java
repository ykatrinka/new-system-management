package ru.clevertec.newsservice.cache.factory.impl;


import ru.clevertec.newsservice.cache.CustomCache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;
import ru.clevertec.newsservice.cache.factory.CacheType;
import ru.clevertec.newsservice.cache.impl.LFUCache;
import ru.clevertec.newsservice.cache.impl.LRUCache;

/**
 * @param <K> тип идентификатора.
 * @param <V> тип объекта.
 *            Фабрика для получения реализации кэша.
 * @author Katerina
 * @version 1.0.0
 */
public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {
    /**
     * Получает реализацию кэша.
     *
     * @param type тип кеширования.
     * @return CustomCache.
     */
    @Override
    public CustomCache<K, V> getInstance(CacheType type) {
        return switch (type) {
            case LFU -> new LFUCache<>();
            case LRU -> new LRUCache<>();
        };
    }
}
