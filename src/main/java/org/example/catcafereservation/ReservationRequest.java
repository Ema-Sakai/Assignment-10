package org.example.catcafereservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    private String name;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String email;
    private String phone;

    public Reservation convertToEntity() {
        return new Reservation(this.name, this.reservationDate, this.reservationTime, this.email, this.phone);
    }
}
