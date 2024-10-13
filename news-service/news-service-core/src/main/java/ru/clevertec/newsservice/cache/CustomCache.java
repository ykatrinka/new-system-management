package ru.clevertec.newsservice.cache;

import java.util.Optional;

/**
 * @param <K> тип идентификатора.
 * @param <V> тип объекта.
 *            Реализация кэша.
 * @author Katerina
 * @version 1.0.0
 */
public interface CustomCache<K, V> {
    /**
     * Добавление объекта в кэш.
     *
     * @param key   идентификатор объекта.
     * @param value объект.
     */
    void put(K key, V value);

    /**
     * Получение объекта из кэша.
     *
     * @param key идентификатор объекта.
     * @return объект.
     */
    Optional<V> get(K key);

    /**
     * Удаление объекта из кэша.
     *
     * @param key идентификатор объекта.
     */
    void delete(K key);
}
