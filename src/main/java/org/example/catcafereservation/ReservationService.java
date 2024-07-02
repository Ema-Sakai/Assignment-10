package org.example.catcafereservation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Reservation insert(String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone) {
        Reservation reservation = new Reservation(name, reservationDate, reservationTime, email, phone);
        reservationMapper.insert(reservation);
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
