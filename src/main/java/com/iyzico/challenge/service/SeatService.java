package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.dto.request.SeatUpdateRequest;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.exception.SeatTakenException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatService {
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final FlightService flightService;
    private final ModelMapper modelMapper;

    @Transactional
    public List<SeatDTO> add(SeatCreateRequest request) throws NotFoundException {
        Flight flight = flightService.getFlight(request.getFlightId());
        List<Seat> seats = request.getSeats().stream().map(this::convertToEntity).collect(Collectors.toList());
        seats.forEach(p -> p.setFlight(flight));
        List<Seat> saved= seatRepository.saveAllAndFlush(seats);
        log.info(seats.size() + " seat added successfully!");
        return saved.stream().map((element) -> modelMapper.map(element, SeatDTO.class)).collect(Collectors.toList());
    }

    public SeatDTO update(Long id, SeatUpdateRequest request) throws NotFoundException {
        Seat seat = getSeat(id);
        seat.setSeatPrice(request.getSeatPrice());

        seatRepository.save(seat);
        log.info("Seat updated successfully!");

        return this.convertToDto(seat);
    }

    public void delete(Long id) throws NotFoundException, SeatTakenException {
        Seat seat = this.getSeat(id);
        if(Boolean.TRUE.equals(seat.getIsSold())){
            throw new SeatTakenException();
        }
        seat.setDeleteFlag(true);
        seatRepository.save(seat);
    }

    public Seat getSeat(Long id) throws NotFoundException {
        return seatRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public SeatDTO convertToDto(Seat entity) {
        return modelMapper.map(entity, SeatDTO.class);
    }

    public Seat convertToEntity(SeatDTO dto) {
        return modelMapper.map(dto, Seat.class);
    }
}
