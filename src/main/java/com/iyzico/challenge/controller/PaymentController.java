package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.PaymentDTO;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.dto.response.PaymentResponse;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> buySeat(@RequestBody PaymentDTO request) throws PaymentException {
        return ResponseEntity.ok().body(paymentService.pay(request));
    }
}
