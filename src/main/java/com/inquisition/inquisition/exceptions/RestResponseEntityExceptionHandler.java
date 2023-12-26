package com.inquisition.inquisition.exceptions;

import java.sql.SQLException;

import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = {SQLException.class, DataAccessException.class})
    protected ResponseEntity<Payload> handleConflictDatabase(
            Exception ex) {
        LOGGER.error("Exception with sql execution: {}", ex.getMessage());
        String message = "Ошибка, попробуйте позже";
        return ResponseEntity.badRequest().body(new BasePayload(400, message));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<Payload> handleAcessDenied(
            Exception ex) {
        LOGGER.error("Access denied: {}", ex.getMessage());
        String message = "Недостаточно прав пользователя для выполнения запроса";
        return ResponseEntity.badRequest().body(new BasePayload(403, message));
    }
}
