package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.dto.request.SeatListUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.exception.SeatTakenException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightService flightService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SeatService seatService;

    private Flight flight;
    private List<Seat> seats;

    @BeforeEach
    public void setUp() {
        SeatCreateRequest seatCreateRequest = new SeatCreateRequest();
        seatCreateRequest.setFlightId(1L);
        seatCreateRequest.setSeats(Collections.singletonList(new SeatDTO(1L, "A1", new BigDecimal(100))));

        flight = new Flight();
        flight.setId(1L);
        flight.setSeats(seats);

        seats = Collections.singletonList(new Seat(1L, "A1", new BigDecimal(100), flight, false));
    }

    @Test
    void testUpdateSeatsWhenSeatPricesAreUpdatedThenReturnUpdatedSeats() throws NotFoundException, SeatTakenException {
        // Arrange
        Map<Long, BigDecimal> idPriceMap = new HashMap<>();
        idPriceMap.put(1L, new BigDecimal(200));
        SeatListUpdateRequest request = new SeatListUpdateRequest(idPriceMap);

        Seat seat = new Seat(1L, "A1", new BigDecimal(100), flight, false);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(modelMapper.map(Mockito.any(Seat.class), Mockito.eq(SeatDTO.class))).thenReturn(new SeatDTO(seat.getId(), seat.getSeatNumber(), BigDecimal.valueOf(200)));
        when(seatRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);

        // Act
        List<SeatDTO> updatedSeats = seatService.updateSeats(request);

        // Assert
        assertEquals(1, updatedSeats.size());
        assertEquals(new BigDecimal(200), updatedSeats.get(0).getSeatPrice());
    }

    @Test
    void testUpdateSeatsWhenSeatIsSoldThenThrowSeatTakenException() throws NotFoundException {
        // Arrange
        Map<Long, BigDecimal> idPriceMap = new HashMap<>();
        idPriceMap.put(1L, new BigDecimal(200));
        SeatListUpdateRequest request = new SeatListUpdateRequest(idPriceMap);

        Seat seat = new Seat(1L, "A1", new BigDecimal(100), flight, true);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // Act & Assert
        assertThrows(SeatTakenException.class, () -> seatService.updateSeats(request));
    }

    @Test
    void testGetAvailableSeatsWhenSeatsAvailableThenReturnSeatDTOList() throws NotFoundException {
        // Arrange
        when(flightService.getFlight(1L)).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndIsSoldFalse(flight)).thenReturn(seats);
        when(modelMapper.map(seats.get(0), SeatDTO.class)).thenReturn(new SeatDTO(1L, "A1", new BigDecimal(100)));

        // Act
        List<SeatDTO> availableSeats = seatService.getAvailableSeats(1L);

        // Assert
        assertEquals(1, availableSeats.size());
        assertEquals("A1", availableSeats.get(0).getSeatNumber());
    }

    @Test
    void testGetAvailableSeatsWhenNoSeatsAvailableThenThrowNotFoundException() throws NotFoundException {
        // Arrange
        when(flightService.getFlight(1L)).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndIsSoldFalse(flight)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> seatService.getAvailableSeats(1L));
    }

    @Test
    void testDelete() throws NotFoundException, SeatTakenException {
        // Arrange
        Long seatId = 1L;
        Seat seat = new Seat();
        seat.setId(seatId);

        Mockito.when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));

        // Act
        seatService.delete(seatId);

        // Assert
        Assertions.assertTrue(seat.getDeleteFlag());
        Mockito.verify(seatRepository, Mockito.times(1)).save(seat);
    }
}