package org.example.catcafereservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    public Reservation findByReservationNumber(String reservationNumber) {
        return reservationMapper.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("お探しの予約情報は存在しません。"));
    }

    public Reservation insert(String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone) {
        Reservation user = new Reservation(name, reservationDate, reservationTime, email, phone);
        reservationMapper.insert(user);
        return user;
    }

}
