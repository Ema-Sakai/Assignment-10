package org.example.catcafereservation;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationUpdateRequest {

    @Nullable
    private LocalDate reservationDate;

    @Nullable
    private LocalTime reservationTime;

    public ReservationUpdateRequest(LocalDate reservationDate, LocalTime reservationTime) {
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
    }
}
