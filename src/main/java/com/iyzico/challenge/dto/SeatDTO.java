package com.iyzico.challenge.dto;

import com.iyzico.challenge.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.FetchType;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private BigDecimal seatPrice;
}
