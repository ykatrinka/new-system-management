package ru.clevertec.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.authservice.dto.response.AuthenticationResponse;
import ru.clevertec.authservice.dto.request.LoginUser;
import ru.clevertec.authservice.dto.request.RegisterUser;
import ru.clevertec.authservice.service.UserService;
import ru.clevertec.authservice.util.BaseURLData;
import ru.clevertec.authservice.util.DataOpenApi;

/**
 * @author Katerina
 * @version 1.0
 * <p>
 * Контроллер для работы с комментариями.
 */
@RestController
@RequestMapping(BaseURLData.BASE_URL_AUTH)
@RequiredArgsConstructor
public class UserController {

    /**
     * Сервисный класс для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Endpoint для регистрации нового пользователя.
     *
     * @param registerUser Данные из запроса для регистрации пользователя.
     * @return AuthenticationResponse с токеном.
     * Данные о пользователе: имя и роль.
     */
    @Operation(summary = DataOpenApi.SUMMARY_REGISTER_USER, tags = DataOpenApi.TAG_AUTH)
    @PostMapping(value = BaseURLData.URL_REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterUser registerUser) {
        return new ResponseEntity<>(userService.register(registerUser), HttpStatus.CREATED);
    }

    /**
     * Endpoint для авторизации пользователя.
     *
     * @param loginUser Данные из запроса для авторизации пользователя.
     * @return AuthenticationResponse с токеном.
     * Данные о пользователе: имя и роль.
     */
    @Operation(summary = DataOpenApi.SUMMARY_AUTH_USER, tags = DataOpenApi.TAG_AUTH)
    @PostMapping(value = BaseURLData.URL_LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody LoginUser loginUser) {
        return new ResponseEntity<>(userService.authenticate(loginUser), HttpStatus.OK);
    }
}
