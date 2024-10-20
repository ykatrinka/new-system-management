package ru.clevertec.newsservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.newsservice.service.JwtService;
import ru.clevertec.newsservice.util.DataSecurity;

import java.io.IOException;

/**
 * @author Katerina
 * @version 1.0.0
 * Проверяет авторизован ли пользователь
 * и не истек ли срок действия токена.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Метод проверяет разрешения на обработку данных.
     * Используется механизм фильтров.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(DataSecurity.HEADER_AUTH);
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith(DataSecurity.HEADER_AUTH_TYPE)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtService.isTokenNotExpired(jwt)
        ) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    jwtService.getAuthority(jwt)
            );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }


        filterChain.doFilter(request, response);
    }
}
