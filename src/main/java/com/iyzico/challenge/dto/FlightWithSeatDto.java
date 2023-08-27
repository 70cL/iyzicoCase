package com.iyzico.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FlightWithSeatDto extends FlightDTO{
    private List<SeatDTO> seats;
}
