package org.example.catcafereservation;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public Optional<Reservation> findByReservationNumber(String reservationNumber) {
        return reservationMapper.findByReservationNumber(reservationNumber);
    }
}
