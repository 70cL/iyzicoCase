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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public List<SeatDTO> updateSeats(SeatListUpdateRequest request) throws NotFoundException, SeatTakenException {
        List<SeatDTO> seatsDTO = new ArrayList<>();
        List<Seat> entitySeats = new ArrayList<>();

        for(Long id : request.getIdPriceMap().keySet()){
            Seat seat = getSeat(id);

            if (Boolean.TRUE.equals((seat.getIsSold()))) {
                throw new SeatTakenException();
            }

            seat.setSeatPrice(request.getIdPriceMap().get(id));
            entitySeats.add(seat);
            seatsDTO.add(convertToDto(seat));
        }

        seatRepository.saveAll(entitySeats);
        log.info("Seats updated successfully!");
        return seatsDTO;
    }

    public List<SeatDTO> getAvailableSeats(Long flightId) throws NotFoundException {
        Flight flight = flightService.getFlight(flightId);
        List<Seat> availableSeats = seatRepository.findSeatsByFlightAndIsSoldFalse(flight);

        if (availableSeats.isEmpty()) {
            log.error("No available seat found for flight: {}", flight.getCallSign());
            throw new NotFoundException();
        }
        return availableSeats.stream().map(element -> modelMapper.map(element, SeatDTO.class)).collect(Collectors.toList());
    }

    public void delete(Long id) throws NotFoundException, SeatTakenException {
        Seat seat = this.getSeat(id);
        if(Boolean.TRUE.equals(seat.getIsSold())){
            throw new SeatTakenException();
        }
        seat.setDeleteFlag(true);
        seatRepository.save(seat);
    }

    @Transactional
    public void updateSeatsStatus(List<Seat> seats) {
        List<Seat> entitySeats = new ArrayList<>();

        for(Seat seat : seats){
            seat.setIsSold(true);
            entitySeats.add(seat);
        }

        seatRepository.saveAll(entitySeats);
        log.info("Seats status updated successfully!");
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
