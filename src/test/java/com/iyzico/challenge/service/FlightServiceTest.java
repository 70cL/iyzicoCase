package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightWithSeatDto;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.FlightCreateRequest;
import com.iyzico.challenge.dto.request.FlightUpdateRequest;
import com.iyzico.challenge.dto.response.PageResponseDTO;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightService flightService;

    @Test
    public void testGetFlightsWhenCalledThenReturnsCorrectData() {
        // Arrange
        Integer page = 0;
        Integer pageSize = 10;
        Flight flight = new Flight();
        flight.setFlightName("Test Flight");
        Page<Flight> flightsPage = new PageImpl<>(Collections.singletonList(flight), PageRequest.of(page, pageSize), 1);

        Mockito.when(flightRepository.findAll(PageRequest.of(page, pageSize))).thenReturn(flightsPage);
        Mockito.when(modelMapper.map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class))).thenReturn(new FlightDTO());

        // Act
        PageResponseDTO result = flightService.getFlights(page, pageSize);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(10, result.getNumberOfElements().intValue());
        Mockito.verify(flightRepository).findAll(PageRequest.of(page, pageSize));
        Mockito.verify(modelMapper).map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class));
    }

    @Test
    public void testGetFlightsWhenNoFlightsThenReturnsEmpty() {
        // Arrange
        Integer page = 0;
        Integer pageSize = 10;
        Page<Flight> flightsPage = Page.empty();

        Mockito.when(flightRepository.findAll(PageRequest.of(page, pageSize))).thenReturn(flightsPage);

        // Act
        PageResponseDTO result = flightService.getFlights(page, pageSize);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.getNumberOfElements().intValue());
        Mockito.verify(flightRepository).findAll(PageRequest.of(page, pageSize));
    }

    @Test
    public void testUpdateWhenFlightDetailsProvidedThenFlightDetailsUpdated() throws NotFoundException {
        // Arrange
        Long flightId = 1L;
        FlightUpdateRequest request = new FlightUpdateRequest();
        request.setFlightName("Updated Flight");
        request.setDescription("Updated Description");

        Flight flight = new Flight();
        flight.setId(flightId);
        flight.setFlightName("Old Flight");
        flight.setDescription("Old Description");

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        Mockito.when(flightRepository.save(Mockito.any(Flight.class))).thenReturn(flight);
        Mockito.when(modelMapper.map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class))).thenAnswer(invocation -> {
            Flight source = invocation.getArgument(0);
            FlightDTO destination = new FlightDTO();
            destination.setFlightName(source.getFlightName());
            destination.setDescription(source.getDescription());
            return destination;
        });

        // Act
        FlightDTO result = flightService.update(flightId, request);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(request.getFlightName(), result.getFlightName());
        Assert.assertEquals(request.getDescription(), result.getDescription());
        Mockito.verify(flightRepository).findById(flightId);
        Mockito.verify(flightRepository).save(Mockito.any(Flight.class));
        Mockito.verify(modelMapper).map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateWhenIdNotFoundThenNotFoundExceptionThrown() throws NotFoundException {
        // Arrange
        Long flightId = 1L;
        FlightUpdateRequest request = new FlightUpdateRequest();
        request.setFlightName("Updated Flight");
        request.setDescription("Updated Description");

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.empty());

        // Act
        flightService.update(flightId, request);

        // Assert
        // Expect NotFoundException
    }

    @Test
    public void testDelete() throws NotFoundException {
        // Arrange
        Long flightId = 1L;
        Flight flight = new Flight();
        flight.setId(flightId);

        Mockito.when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        // Act
        flightService.delete(flightId);

        // Assert
        Assert.assertTrue(flight.getDeleteFlag());
        Mockito.verify(flightRepository).save(flight);
    }

    @Test
    public void testCreate() {
        // Arrange
        FlightCreateRequest request = new FlightCreateRequest();
        request.setFlightName("Test Flight");
        request.setDescription("Test Description");
        request.setTakeOff("Test Take Off");
        request.setLand("Test Land");

        Flight flight = new Flight();
        flight.setFlightName(request.getFlightName());
        flight.setDescription(request.getDescription());
        flight.setTakeOff(request.getTakeOff());
        flight.setLand(request.getLand());

        Mockito.when(flightRepository.saveAndFlush(Mockito.any(Flight.class))).thenReturn(flight);
        Mockito.when(modelMapper.map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class))).thenReturn(new FlightDTO());

        // Act
        FlightDTO result = flightService.create(request);

        // Assert
        Assert.assertNotNull(result);
        Mockito.verify(flightRepository).saveAndFlush(Mockito.any(Flight.class));
        Mockito.verify(modelMapper).map(Mockito.any(Flight.class), Mockito.eq(FlightDTO.class));
    }
}
