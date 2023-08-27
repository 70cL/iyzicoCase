package com.iyzico.challenge.exception;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.message.DbException;
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
    public ResponseEntity<Object> handleSeatTakenException(SeatTakenException seatTakenException, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(seatTakenException);
    }
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Object> handlePaymentException(PaymentException paymentException, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(paymentException);
    }
    @ExceptionHandler(JdbcSQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleException(JdbcSQLIntegrityConstraintViolationException dbException, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dbException);
    }
}
