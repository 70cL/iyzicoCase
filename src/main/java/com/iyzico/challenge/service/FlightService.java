package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightWithSeatDto;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.response.PageResponseDTO;
import com.iyzico.challenge.dto.request.FlightUpdateRequest;
import com.iyzico.challenge.dto.request.FlightCreateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;
    public void delete(Long id) throws NotFoundException {
        Flight flight = this.getFlight(id);
        flight.setDeleteFlag(true);
        flightRepository.save(flight);
    }

    public PageResponseDTO<FlightDTO> getFlights(Integer page, Integer pageSize) {
        Page<Flight> flights = flightRepository.findAll(PageRequest.of(page,pageSize));
        List<FlightDTO> content = flights.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

        return new PageResponseDTO<>(flights.getSize(), flights.getTotalElements(), flights.getTotalPages(), content);
    }

    @Transactional
    public PageResponseDTO<FlightWithSeatDto> getFlightsWithSeats(Integer page, Integer pageSize) {
        Page<Flight> flights = flightRepository.findAll(PageRequest.of(page,pageSize));
        List<FlightWithSeatDto> content = flights.getContent().stream().map(this::convertToDtoTest).collect(Collectors.toList());

        return new PageResponseDTO<>(flights.getContent().size(), flights.getTotalElements(), flights.getTotalPages(), content);
    }

    public FlightDTO create(FlightCreateRequest request) {
        Flight flight = new Flight();
        flight.setFlightName(request.getFlightName());
        flight.setDescription(request.getDescription());
        flight.setTakeOff(request.getTakeOff());
        flight.setLand(request.getLand());

        Flight saved = flightRepository.saveAndFlush(flight);
        log.info("Flight saved successfully!");

        return this.convertToDto(saved);
    }

    public FlightDTO update(Long id, FlightUpdateRequest request) throws NotFoundException {
        Flight flight = getFlight(id);
        flight.setFlightName(request.getFlightName());
        flight.setDescription(request.getDescription());

        flightRepository.save(flight);
        log.info("Flight updated successfully!");

        return this.convertToDto(flight);
    }

    public FlightDTO convertToDto(Flight entity) {
        return modelMapper.map(entity, FlightDTO.class);
    }

    public FlightWithSeatDto convertToDtoTest(Flight entity) {
        List<SeatDTO> seatDTO = entity.getSeats().stream().map(this::convertToDto).collect(Collectors.toList());
        FlightWithSeatDto flightWithSeatDto = modelMapper.map(entity, FlightWithSeatDto.class);
        flightWithSeatDto.setSeats(seatDTO);
        return flightWithSeatDto;
    }

    public SeatDTO convertToDto(Seat entity) {
        return modelMapper.map(entity, SeatDTO.class);
    }

    public Flight getFlight(Long id) throws NotFoundException {
        return flightRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
