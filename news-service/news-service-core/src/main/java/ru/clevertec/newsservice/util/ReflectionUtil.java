package ru.clevertec.newsservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author Katerina
 * @version 1.0.0
 * Класс для работы с рефлексией.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtil {

    /**
     * Метод для получения полей сущности с указанной аннотацией.
     *
     * @param clazz      Тип класса.
     * @param annotation Тип аннотации для поиска.
     * @param <T>        Тип сущности.
     * @return Список полей с указанной аннотацией.
     */
    public static <T extends Annotation> List<String> getFieldsByAnnotation(
            Class<?> clazz,
            Class<T> annotation
    ) {
        Field[] fields = clazz.getDeclaredFields();

        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(annotation))
                .map(Field::getName)
                .toList();
    }
}
