package ru.clevertec.commentsservice.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.commentsservice.exception.CommentNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Katerina
 * @version 1.0.0
 * Контроллер для формирования сообщений об выброшенном исключении.
 * Формирование сообщений зависит от типа выброшенного исключения.
 */
@RestControllerAdvice
public class ExceptionHandlingController {

    /**
     * Сообщение при ошибочно набранном идентификаторе комментария.
     *
     * @param e Тип выброшенного исключения
     * @return ErrorMessage
     * Данные со статусом и описанием ошибки.
     */
    @ExceptionHandler(value = {CommentNotFoundException.class})
    public ResponseEntity<ErrorMessage> handleNewsNotFoundExceptions(CommentNotFoundException e) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Сообщение при ошибках валидации (не корректные данные).
     *
     * @param e Тип выброшенного исключения
     * @return ErrorMessage
     * Данные со статусом и описанием ошибок.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException e) {

        Map<String, String> mapErrors = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors()
                .forEach(error -> {
                    String field = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
                    String message = error.getDefaultMessage();
                    if (mapErrors.containsKey(field)) {
                        mapErrors.put(field, mapErrors.get(field) + " = " + message);
                    } else {
                        mapErrors.put(field, message);
                    }
                });

        ErrorMessage errorMessage = ErrorMessage
                .builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(mapErrors.toString())
                .build();

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Сообщение при прочих не описанных ошибках.
     *
     * @param e Тип выброшенного исключения
     * @return ErrorMessage
     * Данные со статусом и описанием ошибки.
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorMessage> handleExceptions(Exception e) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
