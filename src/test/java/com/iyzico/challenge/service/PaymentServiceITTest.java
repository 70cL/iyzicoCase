package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.PaymentDTO;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.FlightCreateRequest;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.exception.PaymentException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAsync
public class PaymentServiceITTest {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private SeatService seatService;
    @Autowired
    private FlightService flightService;

    @Test
    public void pay() throws InterruptedException, NotFoundException {
        FlightCreateRequest flightReq = new FlightCreateRequest();
        flightReq.setFlightName("Test");
        flightReq.setDescription("IT");
        FlightDTO flightDTO = flightService.create(flightReq);
        SeatDTO seat = new SeatDTO();
        seat.setSeatPrice(BigDecimal.ONE);
        seat.setSeatNumber("A12");
        List<SeatDTO> seatDTOS = new ArrayList<>();
        seatDTOS.add(seat);
        SeatCreateRequest seatCreateRequest = new SeatCreateRequest(flightDTO.getId(), seatDTOS);

        List<SeatDTO> createdSeat = seatService.add(seatCreateRequest);
        PaymentDTO paymentDTO = new PaymentDTO(createdSeat.stream().map(SeatDTO::getId).collect(Collectors.toList()));


        final ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2; ++i) {
            executor.execute(() -> {
                try {
                    paymentService.pay(paymentDTO);
                } catch (PaymentException e) {
                    System.err.println(e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Seat savedSeat = seatService.getSeat(createdSeat.get(0).getId());
        Assert.assertNotNull(savedSeat);
        Assert.assertEquals(true, savedSeat.getIsSold());
    }
}
