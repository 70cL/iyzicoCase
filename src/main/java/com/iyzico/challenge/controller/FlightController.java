package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.FlightWithSeatDto;
import com.iyzico.challenge.dto.response.PageResponseDTO;
import com.iyzico.challenge.dto.request.FlightUpdateRequest;
import com.iyzico.challenge.dto.request.FlightCreateRequest;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/flight", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/{page}/{pageSize}")
    public ResponseEntity<PageResponseDTO<FlightDTO>> getFlights(@PathVariable Integer page, @PathVariable Integer pageSize) {
        PageResponseDTO<FlightDTO> flightDTOs = flightService.getFlights(page, pageSize);
        return ResponseEntity.ok().body(flightDTOs);
    }

    @GetMapping("get-flight-with-seats/{page}/{pageSize}")
    public ResponseEntity<PageResponseDTO<FlightWithSeatDto>> getFlightsWithSeat(@PathVariable Integer page, @PathVariable Integer pageSize) {
        PageResponseDTO<FlightWithSeatDto> flightDTOs = flightService.getFlightsWithSeats(page, pageSize);
        return ResponseEntity.ok().body(flightDTOs);
    }

    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@RequestBody FlightCreateRequest request) {
        FlightDTO flightDTO = flightService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(flightDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@PathVariable Long id, @RequestBody FlightUpdateRequest request) throws NotFoundException {
        FlightDTO flightDTO = flightService.update(id, request);
        return ResponseEntity.ok().body(flightDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFlight(@PathVariable Long id) throws NotFoundException {
        flightService.delete(id);
        return ResponseEntity.ok().build();
    }
}
