package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.*;
import com.iyzico.challenge.dto.response.PageResponseDTO;
import com.iyzico.challenge.exception.NotFoundException;
import com.iyzico.challenge.exception.SeatTakenException;
import com.iyzico.challenge.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/seat", produces = MediaType.APPLICATION_JSON_VALUE)
public class SeatController {
    private final SeatService seatService;

    @GetMapping("{id}")
    public ResponseEntity<List<SeatDTO>> getAvailableSeats(@PathVariable Long id) throws NotFoundException {
        List<SeatDTO> seatDTOs = seatService.getAvailableSeats(id);
        return ResponseEntity.ok().body(seatDTOs);
    }

    @PostMapping
    public ResponseEntity<List<SeatDTO>> addSeat(@RequestBody SeatCreateRequest request) throws NotFoundException {
        List<SeatDTO> seatDTO = seatService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatDTO);
    }

    @PutMapping()
    public  ResponseEntity<List<SeatDTO>> updateSeats(@RequestBody SeatListUpdateRequest request) throws NotFoundException, SeatTakenException {
        List<SeatDTO> seatDTO = seatService.updateSeats(request);
        return ResponseEntity.ok().body(seatDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSeat(@PathVariable Long id) throws NotFoundException, SeatTakenException {
        seatService.delete(id);
        return ResponseEntity.ok().build();
    }
}
