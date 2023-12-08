//package com.inquisition.inquisition.controller;
//
//import com.inquisition.inquisition.model.payload.BasePayload;
//import com.inquisition.inquisition.model.payload.Payload;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@ControllerAdvice
//public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(value
//            = { Exception.class })
//    protected ResponseEntity<Payload> handleConflict(
//            RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return ResponseEntity.badRequest().body(new BasePayload(400, "Bad request"));
//    }
//}
