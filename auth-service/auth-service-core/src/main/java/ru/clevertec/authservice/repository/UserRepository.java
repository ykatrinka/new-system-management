package ru.clevertec.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.authservice.entity.User;

import java.util.Optional;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Репозиторий для работы с базой данных.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Получает пользователя по имени.
     *
     * @param username имя пользователя.
     * @return Пользователя из БД.
     */
    Optional<User> findByUsername(String username);
}
