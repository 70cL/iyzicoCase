package com.iyzico.challenge.dto.request;

import com.iyzico.challenge.dto.SeatCreateDTO;
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
    private List<SeatCreateDTO> seats;
}
