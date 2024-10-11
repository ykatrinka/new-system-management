package ru.clevertec.newsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.newsservice.dto.request.NewsRequest;
import ru.clevertec.newsservice.dto.response.NewsResponse;
import ru.clevertec.newsservice.entity.News;

/**
 * @author Katerina
 * @version 1.0.0
 * Mapper для конвертации сущностей связанных с новостью.
 */
@Mapper(componentModel = "spring")
public interface NewsMapper {

    /**
     * Метод конвертирует данные типа NewsRequest в News.
     * Поле id - пропускается.
     *
     * @param newsRequest объект для конвертации
     * @return объект типа News.
     */
    @Mapping(target = "id", ignore = true)
    News requestToNews(NewsRequest newsRequest);

    /**
     * Метод конвертирует данные типа News в NewsResponse.
     *
     * @param news объект для конвертации
     * @return объект типа NewsResponse.
     */
    NewsResponse newsToResponse(News news);

    /**
     * Метод конвертирует данные типа NewsRequest в News.
     *
     * @param newsRequest объект для конвертации.
     * @param newsId      Устанавливаемый идентификатор.
     * @return объект типа News.
     */
    @Mapping(target = "id", source = "newsId")
    News updateFromRequest(Long newsId, NewsRequest newsRequest);

}
