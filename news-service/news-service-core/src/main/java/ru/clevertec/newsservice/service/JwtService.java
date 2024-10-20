package ru.clevertec.newsservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.clevertec.newsservice.util.DataSecurity;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Сервис для работы с JWT токеном.
 */
@Service
public class JwtService {
    /**
     * Ключ для формирования токена.
     */
    @Value(DataSecurity.KEY_SECRET)
    private String secretKey;

    /**
     * Получает имя пользователя из токена.
     *
     * @param token токен.
     * @return имя пользователя.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Получает роль пользователя из токена.
     *
     * @param token токен.
     * @return роль.
     */
    public List<SimpleGrantedAuthority> getAuthority(String token) {
        final Claims claims = extractAllClaims(token);
        String roleName = claims.get(DataSecurity.JWT_ROLE).toString();
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Проверяет срок действия токена.
     *
     * @param token токен.
     * @return true если срок токен истек, иначе false.
     */
    public boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    /**
     * Считывает срок действия токена.
     *
     * @param token токен.
     * @return срок действия.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
