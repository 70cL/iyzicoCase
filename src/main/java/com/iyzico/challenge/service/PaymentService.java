package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.PaymentDTO;
import com.iyzico.challenge.dto.response.PaymentResponse;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.PaymentException;
import com.iyzico.challenge.exception.SeatTakenException;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzipay.model.Payment;
import com.iyzipay.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IyzicoSandboxPaymentIntegration iyzicoSandboxPaymentIntegration;
    private final SeatService seatService;

    public PaymentResponse pay(PaymentDTO paymentDTO) throws PaymentException {
        List<Seat> seats = new ArrayList<>();
        for (Long id : paymentDTO.getSeatIds()) {
            try {
                Seat seat = seatService.getSeat(id);
                if(Boolean.TRUE.equals(seat.getIsSold())){
                    throw new SeatTakenException();
                }
                seats.add(seat);
            }
            catch (Exception ex) {
                log.error("Selected Seat already sold");
                throw new PaymentException(ex.getMessage() + " Payment failed.");
            }
        }

        Payment iyzicoPaymentResponse = iyzicoSandboxPaymentIntegration.pay(seats);
        PaymentResponse response = new PaymentResponse();
        response.setResult(Status.SUCCESS.getValue().equals(iyzicoPaymentResponse.getStatus()) ? Status.SUCCESS.getValue() : Status.FAILURE.getValue());
        response.setPaymentId(iyzicoPaymentResponse.getPaymentId());
        if(Status.SUCCESS.getValue().equals(iyzicoPaymentResponse.getStatus())){
            save(response, seats);
        }

        return response;
    }

    public void save(PaymentResponse response, List<Seat> seats) {
        seatService.updateSeatsStatus(seats);
        savePayment(response, seats);
        log.info("Payment Id : {} - Payment Result : {} - Payment completed!", response.getPaymentId(), response.getResult());
    }

    public void savePayment(PaymentResponse response, List<Seat> seats) {
        for(Seat seat : seats) {
            com.iyzico.challenge.entity.Payment payment = new com.iyzico.challenge.entity.Payment();
            payment.setPrice(seat.getSeatPrice());
            payment.setSeat(seat);
            payment.setFlight(seat.getFlight());
            payment.setBankResponse(response.getResult());
            paymentRepository.save(payment);
        }
    }
}
