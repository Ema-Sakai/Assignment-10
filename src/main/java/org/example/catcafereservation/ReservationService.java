package org.example.catcafereservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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

    @Transactional
    public Reservation insert(Reservation reservation) {
        reservationMapper.insert(reservation);

        String reservationNumber = generateReservationNumber(reservation.getReservationDate(), reservation.getId());
        reservationMapper.insertReservationNumber(reservationNumber, reservation.getId());

        return reservation;
    }

    public String generateReservationNumber(LocalDate reservationDate, Integer id) {
        String randomChars = generateRandomChars();
        String timePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("mmss"));
        return randomChars + timePart + id;
    }

    private String generateRandomChars() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomChars = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 2; i++) {
            randomChars.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return randomChars.toString();
    }
}
