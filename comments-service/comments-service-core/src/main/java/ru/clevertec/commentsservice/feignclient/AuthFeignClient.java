package ru.clevertec.commentsservice.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.clevertec.commentsservice.dto.request.LoginUser;
import ru.clevertec.commentsservice.dto.response.AuthenticationResponse;
import ru.clevertec.commentsservice.util.DataSecurity;

/**
 * Класс для работы с сервисом security.
 *
 * @author Katerina
 * @version 1.0.0
 */
@FeignClient(
        name = DataSecurity.CLIENT_NAME,
        url = DataSecurity.CLIENT_URL
)
public interface AuthFeignClient {

    /**
     * Получает токен.
     *
     * @param loginUser данные авторизации.
     * @return токен.
     */
    @GetMapping(path = DataSecurity.URL_SEC_AUTH_TOKEN)
    ResponseEntity<AuthenticationResponse> getToken(@RequestBody LoginUser loginUser);
}
