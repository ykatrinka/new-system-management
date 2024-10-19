package ru.clevertec.authservice.conf;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.authservice.filter.JwtAuthenticationFilter;
import ru.clevertec.authservice.util.Constants;

/**
 * @author Katerina
 * @version 1.0.0
 * Конфигурация авторизационного фильтра.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    /**
     * JWT фильтр.
     */
    private final JwtAuthenticationFilter jwtAuthFilter;
    /**
     * Авторизационный провайдер.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * Bean с установленными фильтрами.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception .
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Constants.URL_SEC_AUTH)
                        .permitAll()
                        .requestMatchers(Constants.URL_SEC_OPEN_API)
                        .permitAll()
                        .requestMatchers(Constants.URL_SEC_OPEN_API_DOC)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
