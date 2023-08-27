package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.FlightDTO;
import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.dto.request.FlightCreateRequest;
import com.iyzico.challenge.dto.request.FlightUpdateRequest;
import com.iyzico.challenge.dto.request.SeatCreateRequest;
import com.iyzico.challenge.dto.request.SeatUpdateRequest;
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
@RequestMapping(value = "/api/v1/seat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class SeatController {
    private final SeatService seatService;

//    @GetMapping
//    public ResponseEntity<PageResponseDTO<SeatDTO>> getSeats(Integer page, Integer pageSize, Integer flightId) throws NotFoundException {
//        PageResponseDTO<SeatDTO> seatDTOs = seatService.(page, pageSize, flightId);
//        return ResponseEntity.ok().body(seatDTOs);
//    }

    @PostMapping
    public ResponseEntity<List<SeatDTO>> addSeat(@Valid @RequestBody SeatCreateRequest request) throws NotFoundException {
        List<SeatDTO> seatDTO = seatService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatDTO);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<SeatDTO> updateSeat(@PathVariable Long id, @Valid @RequestBody SeatUpdateRequest request) throws NotFoundException {
        SeatDTO seatDTO = seatService.update(id, request);
        return ResponseEntity.ok().body(seatDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSeat(@PathVariable Long id) throws NotFoundException, SeatTakenException {
        seatService.delete(id);
        return ResponseEntity.ok().build();
    }
}
