package org.example.catcafereservation;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private Integer id;
    private String name;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String email;
    private String phone;

    public Reservation(Integer id, String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.email = email;
        this.phone = phone;
    }

    public Reservation(String name, LocalDate reservationDate, LocalTime reservationTime, String email, String phone) {
        this.id = null;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.email = email;
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
