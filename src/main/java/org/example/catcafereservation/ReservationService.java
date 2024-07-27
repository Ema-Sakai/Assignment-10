package org.example.catcafereservation;

import de.huxhorn.sulky.ulid.ULID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationValidator reservationValidator;
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
        reservationValidator.validateReservationNumber(reservationNumber);

        Reservation reservation = reservationMapper.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("お探しの予約情報は存在しません。"));

        LocalDate currentReservationDate = reservation.getReservationDate();
        LocalDate newReservationDate = updateRequest.getReservationDate();
        LocalTime newReservationTime = updateRequest.getReservationTime();

        reservationValidator.validateReservationUpdate(currentReservationDate, newReservationDate, newReservationTime);
        reservationValidator.validateNoChanges(currentReservationDate, reservation.getReservationTime(), newReservationDate, newReservationTime);

        reservation.setReservationDate(newReservationDate);
        reservation.setReservationTime(newReservationTime);

        reservationMapper.update(reservation);
        return reservation;
    }
}
