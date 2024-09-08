package org.example.catcafereservation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Reservation {

    private Integer id;
    private String name;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String email;
    private String phone;
    private String reservationNumber;

    public Reservation(String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone) {
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.email = email;
        this.phone = phone;
    }

    public Reservation(String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone, String reservationNumber) {
        this.id = null;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.email = email;
        this.phone = phone;
        this.reservationNumber = reservationNumber;
    }
}
