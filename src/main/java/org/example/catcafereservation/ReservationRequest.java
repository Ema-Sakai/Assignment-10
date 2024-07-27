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

    @NotBlank(message = "{reservation.name.notBlank}", groups = ValidationGroups.NotBlankGroup.class)
    @Size(max = 50, message = "{reservation.name.size}", groups = ValidationGroups.PatternGroup.class)
    private String name;

    @NotNull(message = "{reservation.date.notNull}", groups = ValidationGroups.NotBlankGroup.class)
    @Future(message = "{reservation.date.future}", groups = ValidationGroups.PatternGroup.class)
    private LocalDate reservationDate;

    @NotNull(message = "{reservation.time.notNull}", groups = ValidationGroups.NotBlankGroup.class)
    @ValidReservationTime(message = "{reservation.time.invalid}", groups = ValidationGroups.PatternGroup.class)
    private LocalTime reservationTime;

    @NotBlank(message = "{reservation.email.notBlank}", groups = ValidationGroups.NotBlankGroup.class)
    @Email(message = "{reservation.email.valid}", groups = ValidationGroups.PatternGroup.class)
    private String email;

    @NotBlank(message = "{reservation.phone.notBlank}", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]{11}$", message = "{reservation.phone.pattern}", groups = ValidationGroups.PatternGroup.class)
    private String phone;

    public Reservation convertToEntity() {
        return new Reservation(this.name, this.reservationDate, this.reservationTime, this.email, this.phone);
    }
}
