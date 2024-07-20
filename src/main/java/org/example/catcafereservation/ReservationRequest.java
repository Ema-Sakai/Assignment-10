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

    @NotBlank(message = "予約名の入力は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @Size(max = 50, message = "予約名は50文字以内で入力してください。", groups = ValidationGroups.PatternGroup.class)
    private String name;

    //予約日時はフロント側で入力規則（画面上での選択方法）をコントロールできるためNotNullを採用。
    @NotNull(message = "ご希望の予約日選択は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @Future(message = "明日以降のご希望日を選択してください。", groups = ValidationGroups.PatternGroup.class)
    private LocalDate reservationDate;

    @NotNull(message = "ご希望の予約時間選択は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @ValidReservationTime(groups = ValidationGroups.PatternGroup.class)
    private LocalTime reservationTime;

    @NotBlank(message = "メールアドレスの入力は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @Email(message = "有効なメールアドレスを入力してください。", groups = ValidationGroups.PatternGroup.class)
    private String email;

    @NotBlank(message = "電話番号の入力は必須です。", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]{11}$", message = "電話番号を数字のみ11桁で入力してください。（例：09012345678）", groups = ValidationGroups.PatternGroup.class)
    private String phone;

    public Reservation convertToEntity() {
        return new Reservation(this.name, this.reservationDate, this.reservationTime, this.email, this.phone);
    }
}
