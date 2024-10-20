package ru.clevertec.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.authservice.mapper.UserMapper;
import ru.clevertec.authservice.repository.UserRepository;
import ru.clevertec.authservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Конфигурация получения пользователя.
 */
@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    /**
     * Класс репозитория для работы с базой данных.
     */
    private final UserRepository userRepository;

    /**
     * Mapper для конвертации сущностей пользователя.
     */
    private final UserMapper userMapper;

    /**
     * Bean с данными пользователя.
     *
     * @return UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(userMapper::userToUserDetail)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.ERROR_USER_NOT_FOUND));
    }


    /**
     * Bean провайдера для аутентификации.
     *
     * @return AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean менеджера аутентификации.
     *
     * @return AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean кодирования пароля.
     *
     * @return PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
