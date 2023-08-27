package com.iyzico.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "DELETE_FLAG = false")
public class Flight extends AbstractEntityBase{
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String callSign;

    private String flightName;
    private String takeOff;
    private String land;

    private String description;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "flight")
    private List<Seat> seats;

    @PrePersist
    private void onInsert(){
        setCallSign(UUID.randomUUID().toString());
    }
}
