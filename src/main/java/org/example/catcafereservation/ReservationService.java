package org.example.catcafereservation;

import de.huxhorn.sulky.ulid.ULID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ULID ulid = new ULID();

    public Reservation findByReservationNumber(String reservationNumber) {
        return reservationMapper.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("お探しの予約情報は存在しません。"));
    }

    @Transactional
    public Reservation insert(Reservation reservation) {
        reservationMapper.insert(reservation);

        String reservationNumber = generateReservationNumber();
        reservationMapper.insertReservationNumber(reservationNumber, reservation.getId());

        reservation.setReservationNumber(reservationNumber);
        return reservation;
    }

    public String generateReservationNumber() {
        return ulid.nextValue().toString();
    }

    @Transactional
    public Reservation updateReservation(String reservationNumber, ReservationUpdateRequest updateRequest) {
        Reservation reservation = reservationMapper.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("お探しの予約情報は存在しません。"));

        reservation.setReservationDate(updateRequest.getReservationDate());
        reservation.setReservationTime(updateRequest.getReservationTime());

        reservationMapper.update(reservation);

        return reservation;
    }
}
