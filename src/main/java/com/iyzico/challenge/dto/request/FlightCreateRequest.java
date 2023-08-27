package com.iyzico.challenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightCreateRequest {
    private String flightName;
    private String description;
    private String takeOff;
    private String land;
}
