package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.PaymentDTO;
import com.iyzico.challenge.dto.response.PaymentResponse;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.repository.PaymentRepository;
import com.iyzipay.model.Cancel;
import com.iyzipay.model.Payment;
import com.iyzipay.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final IyzicoSandboxPaymentIntegration iyzicoSandboxPaymentIntegration;
    private final SeatService seatService;

    public PaymentResponse pay(PaymentDTO paymentDTO) {
        List<Seat> seats = new ArrayList<>();
        for (Long id : paymentDTO.getSeatIds()) {
            try {
                Seat seat = seatService.getSeat(id);
                seats.add(seat);
            }
            catch (Exception ex) {
                log.error("Selected Seat already sold or removed");
                //throw new PaymentException(ErrorCode.SEAT_ALREADY_SOLD);
            }
        }
        Payment iyzicoPaymentResponse = iyzicoSandboxPaymentIntegration.pay(seats);
        PaymentResponse response = new PaymentResponse();
        response.setResult(Status.SUCCESS.getValue().equals(iyzicoPaymentResponse.getStatus()) ? Status.SUCCESS.getValue() : Status.FAILURE.getValue());
        response.setPaymentId(iyzicoPaymentResponse.getPaymentId());
        log.info("Payment Id : {} - Payment Result : {} - Iyzico Payment completed!", response.getPaymentId(), response.getResult());
        return response;
    }

//    public void save(PaymentDTO paymentDTO) {
//        productService.updateStock(paymentDTO.getProduct().getId(), paymentDTO.getMerchantId(), paymentDTO.getProductQuantity());
//        savePayment(paymentDTO, paymentDTO.getMerchantId());
//        log.info("Order Id : {} - Payment Id : {} - Payment Result : {} - Payment completed!", paymentDTO.getOrderId(), paymentDTO.getPaymentId(), paymentDTO.getResult());
//    }
//
//    public void cancel(String orderId, String paymentId, String locale) {
//        try {
//            Cancel iyzicoCancelResponse = iyzicoSandboxPaymentIntegration.cancel(orderId, paymentId, locale);
//            if (Status.SUCCESS.getValue().equals(iyzicoCancelResponse.getStatus())) {
//                log.error("Order Id : {} - Payment Id : {} - Payment canceled successfully!", orderId, paymentId);
//            } else {
//                log.error("Order Id : {} - Payment Id : {} - Payment not canceled successfully!", orderId, paymentId);
//            }
//        } catch (Exception e) {
//            log.error("Order Id : {} - Payment Id : {} - Payment not canceled successfully!", orderId, paymentId);
//        }
//    }
//
//    private void savePayment(PaymentDTO paymentDTO, Long merchantId) {
//        com.iyzico.challenge.entity.Payment payment = new com.iyzico.challenge.entity.Payment();
//        payment.setPrice(paymentDTO.getTotalAmount());
//        payment.setOrderId(paymentDTO.getOrderId());
//        payment.setBankResponse(paymentDTO.getResult().toString());
//        payment.setMerchantId(merchantId);
//        paymentRepository.save(payment);
//    }
}
