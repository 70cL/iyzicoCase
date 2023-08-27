package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.dto.request.SeatUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SeatService seatService;

    private SeatCreateRequest seatCreateRequest;
    private Flight flight;
    private List<Seat> seats;

//    @BeforeEach
//    public void setUp() {
//        seatCreateRequest = new SeatCreateRequest();
//        seatCreateRequest.setFlightId(1L);
//        seatCreateRequest.setSeats(Collections.singletonList(new SeatDTO("A1", new BigDecimal(100))));
//
//        flight = new Flight();
//        flight.setId(1L);
//        flight.setSeats(seats);
//
//        seats = Collections.singletonList(new Seat(1L, "A1", new BigDecimal(100), flight, false));
//    }
//
//    @Test
//    void testAddWhenGetFlightThrowsNotFoundExceptionThenThrowNotFoundException() throws NotFoundException {
//        when(flightService.getFlight(seatCreateRequest.getFlightId())).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, () -> seatService.add(seatCreateRequest));
//    }
//
//    @Test
//    void testUpdateWhenSeatPriceChangedThenReturnUpdatedSeat() throws NotFoundException {
//        Seat seat = new Seat(1L, "A1", new BigDecimal(100), flight, false);
//        SeatDTO seatDTO = new SeatDTO(1L,"A1", new BigDecimal(200));
//        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest(new BigDecimal(200));
//
//        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
//        when(modelMapper.map(seat, SeatDTO.class)).thenReturn(seatDTO);
//
//        SeatDTO updatedSeatDTO = seatService.update(1L, seatUpdateRequest);
//
//        assertEquals(new BigDecimal(200), updatedSeatDTO.getSeatPrice());
//    }
//
//    @Test
//    void testUpdateWhenSeatUpdatedThenSaveMethodCalled() throws NotFoundException {
//        Seat seat = new Seat(1L, "A1", new BigDecimal(100), flight, false);
//        SeatUpdateRequest seatUpdateRequest = new SeatUpdateRequest(new BigDecimal(200));
//        SeatDTO seatDTO = new SeatDTO(1L,"A1", new BigDecimal(200));
//
//        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
//        when(modelMapper.map(seat, SeatDTO.class)).thenReturn(seatDTO);
//
//        SeatDTO result = seatService.update(1L, seatUpdateRequest);
//
//        assertEquals(seatDTO.getSeatPrice(), result.getSeatPrice());
//        verify(seatRepository, times(1)).save(seat);
//    }
//
//    @Test
//    void testUpdateWhenSeatNotFoundThenThrowNotFoundException() {
//        when(seatRepository.findById(1L)).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> seatService.update(1L, new SeatUpdateRequest(new BigDecimal(200))));
//    }
}