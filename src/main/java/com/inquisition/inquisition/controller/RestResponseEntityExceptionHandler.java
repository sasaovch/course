package com.inquisition.inquisition.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import com.inquisition.inquisition.jwt.AuthEntryPointJwt;
import com.inquisition.inquisition.model.payload.BasePayload;
import com.inquisition.inquisition.model.payload.Payload;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    @ExceptionHandler(value
            = { SQLException.class, InvocationTargetException.class})
    protected ResponseEntity<Payload> handleConflictDatabase(
            Exception ex, WebRequest request) {
        logger.error("SQLException: {}", ex.getMessage());
        String message = "Error happened during handling request. Try later";
        return ResponseEntity.badRequest().body(new BasePayload(400, message));
    }
}
