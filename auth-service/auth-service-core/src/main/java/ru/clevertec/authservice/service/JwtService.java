package ru.clevertec.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.entity.CustomUserDetails;
import ru.clevertec.authservice.util.Constants;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    @Value(Constants.KEY_SECRET)
    private String secretKey;

    /**
     * Срок действия токена.
     */
    @Value(Constants.EXPIRATION_TIME)
    private int jwtExpiration;

    /**
     * Получает имя пользователя из токена.
     *
     * @param token токен.
     * @return имя пользователя.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Метод формирует токен.
     *
     * @param userDetails данные пользователя.
     * @return токен.
     */
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(Constants.JWT_ROLE, ((CustomUserDetails) userDetails).getRole());
        return buildToken(extraClaims, userDetails);
    }

    /**
     * Генерирует токен на основании переданных данных.
     *
     * @param userDetails данные пользователя.
     * @return токен.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token       токен.
     * @param userDetails данные пользователя.
     * @return true если токен валидный, иначе false.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Проверяет срок действия токена.
     *
     * @param token токен.
     * @return true если срок токен истек, иначе false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
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
