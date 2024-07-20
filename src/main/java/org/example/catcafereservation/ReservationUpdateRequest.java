package org.example.catcafereservation;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationUpdateRequest {

    @NotNull(message = "ご希望の予約日選択は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @Future(message = "明日以降のご希望日を選択してください。", groups = ValidationGroups.PatternGroup.class)
    private LocalDate reservationDate;

    @NotNull(message = "ご希望の予約時間選択は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @ValidReservationTime(groups = ValidationGroups.PatternGroup.class)
    private LocalTime reservationTime;
}
