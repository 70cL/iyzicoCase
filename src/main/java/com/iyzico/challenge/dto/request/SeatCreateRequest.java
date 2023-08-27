package com.iyzico.challenge.dto.request;

import com.iyzico.challenge.dto.SeatDTO;
import com.iyzico.challenge.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatCreateRequest {
    private Long flightId;
    private List<SeatDTO> seats;
}
