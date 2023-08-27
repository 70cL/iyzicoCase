package com.iyzico.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"seatNumber", "flight_id"})
})
@Where(clause = "DELETE_FLAG = false")
public class Seat extends AbstractEntityBase{
    @Id
    @GeneratedValue
    private Long id;

    private String seatNumber;

    @NonNull
    private BigDecimal seatPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    private Flight flight;

    private Boolean isSold=false;
}
