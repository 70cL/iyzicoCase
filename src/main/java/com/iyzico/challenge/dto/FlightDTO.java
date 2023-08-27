package com.iyzico.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String flightName;
    private String callSign;
    private String description;
    private String takeOff;
    private String land;
}
