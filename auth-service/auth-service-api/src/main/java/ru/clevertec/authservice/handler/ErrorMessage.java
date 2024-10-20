package ru.clevertec.authservice.handler;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author Katerina
 * @version 1.0.0
 * Класс для формирования сообщения об ошибке.
 */
@Getter
@AllArgsConstructor
@Builder
public class ErrorMessage {
    /**
     * Код ошибки.
     */
    private int statusCode;
    /**
     * Время возникновения ошибки.
     */
    private LocalDateTime timeStamp;
    /**
     * Сообщение с описанием ошибки.
     */
    private String message;
}
