package ru.clevertec.authservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.authservice.dto.request.LoginUser;
import ru.clevertec.authservice.dto.request.RegisterUser;
import ru.clevertec.authservice.dto.response.AuthenticationResponse;
import ru.clevertec.authservice.entity.CustomUserDetails;
import ru.clevertec.authservice.entity.User;
import ru.clevertec.authservice.exception.UserNotFoundException;
import ru.clevertec.authservice.exception.UserNotUniqueException;
import ru.clevertec.authservice.mapper.UserMapper;
import ru.clevertec.authservice.repository.UserRepository;

/**
 * * @author Katerina
 * * @version 1.0.0
 * Сервис для работы с сущностью "Пользователь".
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Регистрирует нового пользователя.
     *
     * @param userRequest данные пользователя.
     * @return токен созданного пользователя.
     */
    public AuthenticationResponse register(RegisterUser userRequest) {
        User user = userRepository
                .findByUsername(userRequest.username())
                .orElse(null);
        if (user != null) {
            throw UserNotUniqueException.getByUsername(userRequest.username());
        }

        user = User.builder()
                .username(userRequest.username())
                .password(passwordEncoder.encode(userRequest.password()))
                .role(userRequest.role())
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(userMapper.userToUserDetail(user));
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    /**
     * Авторизация пользователя.
     *
     * @param loginUser данные пользователя.
     * @return токен созданного пользователя.
     */
    public AuthenticationResponse authenticate(LoginUser loginUser) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.username(),
                        loginUser.password()
                )
        );

        CustomUserDetails userDetails = userRepository.findByUsername(loginUser.username())
                .map(userMapper::userToUserDetail)
                .orElseThrow(() -> UserNotFoundException.getByUsername(loginUser.username()));

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(userDetails))
                .build();
    }
}
