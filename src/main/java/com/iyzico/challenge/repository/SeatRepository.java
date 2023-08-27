package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findSeatsByFlightAndIsSoldFalse(Flight flight);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Seat> findById(Long id);
}
