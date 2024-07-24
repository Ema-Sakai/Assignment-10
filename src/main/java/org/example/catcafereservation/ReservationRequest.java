package org.example.catcafereservation;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotBlank(message = "{reservation.name.notBlank}")
    @Size(max = 50, message = "{reservation.name.size}")
    private String name;

    @NotNull(message = "{reservation.date.notNull}")
    @Future(message = "{reservation.date.future}")
    private LocalDate reservationDate;

    @NotNull(message = "{reservation.time.notNull}")
    @ValidReservationTime(message = "{reservation.time.invalid}")
    private LocalTime reservationTime;

    @NotBlank(message = "{reservation.email.notBlank}")
    @Email(message = "{reservation.email.valid}")
    private String email;

    @NotBlank(message = "{reservation.phone.notBlank}")
    @Pattern(regexp = "^[0-9]{11}$", message = "{reservation.phone.pattern}")
    private String phone;

    public Reservation convertToEntity() {
        return new Reservation(this.name, this.reservationDate, this.reservationTime, this.email, this.phone);
    }
}
