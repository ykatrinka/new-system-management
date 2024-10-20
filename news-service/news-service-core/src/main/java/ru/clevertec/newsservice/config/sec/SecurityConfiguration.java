package ru.clevertec.newsservice.config.sec;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.clevertec.newsservice.filter.JwtAuthenticationFilter;
import ru.clevertec.newsservice.util.DataSecurity;

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
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/{newsId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/news").hasAnyAuthority(DataSecurity.ROLE_ADMIN, DataSecurity.ROLE_JOURNALIST)
                        .requestMatchers(HttpMethod.PUT, "/news/{newsId}").hasAnyAuthority(DataSecurity.ROLE_ADMIN, DataSecurity.ROLE_JOURNALIST)
                        .requestMatchers(HttpMethod.DELETE, "/news/{newsId}").hasAnyAuthority(DataSecurity.ROLE_ADMIN, DataSecurity.ROLE_JOURNALIST)
                        .requestMatchers(HttpMethod.GET, "/news/{newsId}/comments/{commentsId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/{newsId}/comments").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
