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
        if (reservationNumber == null || reservationNumber.length() != 26) {
            throw new ReservationUpdateValidationException("予約番号を確認してください。");
        }

        Reservation reservation = reservationMapper.findByReservationNumber(reservationNumber)
                .orElseThrow(() -> new ReservationNotFoundException("お探しの予約情報は存在しません。"));

        LocalDate currentReservationDate = reservation.getReservationDate();
        LocalDate newReservationDate = updateRequest.getReservationDate();
        LocalTime newReservationTime = updateRequest.getReservationTime();

        // 既存の予約日が過去の日付である場合にエラーをスロー
        if (currentReservationDate.isBefore(LocalDate.now())) {
            throw new ReservationUpdateValidationException("過去の予約情報は更新できません。");
        }

        Map<String, String> errors = new HashMap<>();

        if (newReservationDate == null || !newReservationDate.isAfter(LocalDate.now())) {
            errors.put("reservationDate", "予約日は翌日以降の日付を選択してください。");
        }

        if (newReservationTime == null || newReservationTime.getHour() < 11 || newReservationTime.getHour() > 14 || (newReservationTime.getMinute() != 0 && newReservationTime.getMinute() != 30)) {
            errors.put("reservationTime", "予約時間は11:00から14:00までの間で、30分単位で選択してください。（例: 11:00、11:30）");
        }

        if (!errors.isEmpty()) {
            throw new ReservationUpdateValidationException("予約更新情報が無効です。", errors);
        }

        if (newReservationDate.equals(currentReservationDate) && newReservationTime.equals(reservation.getReservationTime())) {
            throw new ReservationUpdateValidationException("現在の予約情報と同じです。変更する場合は、異なる予約日時を指定してください。");
        }

        reservation.setReservationDate(newReservationDate);
        reservation.setReservationTime(newReservationTime);

        reservationMapper.update(reservation);
        return reservation;
    }
}
