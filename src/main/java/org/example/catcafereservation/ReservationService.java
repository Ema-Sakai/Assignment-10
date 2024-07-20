package org.example.catcafereservation;

import de.huxhorn.sulky.ulid.ULID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

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

        Map<String, String> errors = new HashMap<>();
        boolean isUpdated = false;
        boolean hasChange = false;

        if (updateRequest.getReservationDate() != null) {
            if (!updateRequest.getReservationDate().equals(reservation.getReservationDate())) {
                hasChange = true;
                try {
                    validateReservationDate(updateRequest.getReservationDate());
                    reservation.setReservationDate(updateRequest.getReservationDate());
                    isUpdated = true;
                } catch (ReservationUpdateValidationException e) {
                    errors.putAll(e.getErrors());
                }
            }
        }

        if (updateRequest.getReservationTime() != null) {
            if (!updateRequest.getReservationTime().equals(reservation.getReservationTime())) {
                hasChange = true;
                try {
                    validateReservationTime(updateRequest.getReservationTime());
                    reservation.setReservationTime(updateRequest.getReservationTime());
                    isUpdated = true;
                } catch (ReservationUpdateValidationException e) {
                    errors.putAll(e.getErrors());
                }
            }
        }

        if (!hasChange) {
            throw new ReservationUpdateValidationException("更新情報を選択してください。", Map.of("update", "現在の予約情報と同じです。変更する場合は、異なる予約日時を指定してください。"));
        }

        if (!errors.isEmpty()) {
            throw new ReservationUpdateValidationException("予約更新情報が無効です。", errors);
        }

        if (!isUpdated) {
            throw new ReservationUpdateValidationException("更新する予約情報が指定されていません。", Map.of("update", "予約日または予約時間を指定してください。"));
        }

        reservationMapper.update(reservation);
        return reservation;
    }

    private void validateReservationDate(LocalDate date) {
        if (!date.isAfter(LocalDate.now())) {
            throw new ReservationUpdateValidationException("予約日が無効です。", Map.of("reservationDate", "予約日は翌日以降の日付を選択してください。"));
        }
    }

    private void validateReservationTime(LocalTime time) {
        if (time.getHour() < 11 || time.getHour() > 14 || (time.getMinute() != 0 && time.getMinute() != 30)) {
            throw new ReservationUpdateValidationException("予約時間が無効です。", Map.of("reservationTime", "予約時間は11:00から14:00までの間から、30分単位で選択してください。（例: 11:00、11:30）"));
        }
    }
}
