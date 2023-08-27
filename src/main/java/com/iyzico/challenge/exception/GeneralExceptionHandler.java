package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementFoundException(NotFoundException notFoundException, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException);
    }

    @ExceptionHandler(SeatTakenException.class)
    public ResponseEntity<Object> handleDuplicateUserException(SeatTakenException seatTakenException, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(seatTakenException);
    }
}
