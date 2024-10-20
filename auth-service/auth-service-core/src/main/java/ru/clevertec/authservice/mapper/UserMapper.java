package ru.clevertec.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.authservice.entity.CustomUserDetails;
import ru.clevertec.authservice.entity.User;

/**
 * @author Katerina
 * @version 1.0.0
 * Mapper для конвертации сущностей связанных с пользователем.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Метод конвертирует данные типа User в CustomUserDetails.
     *
     * @param user объект для конвертации
     * @return объект типа CustomUserDetails.
     */
    @Mapping(target = "authorities", ignore = true)
    CustomUserDetails userToUserDetail(User user);

}
