package org.example.catcafereservation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class ReservationUpdateValidator implements ConstraintValidator<ValidReservationUpdate, Object[]> {

    @Autowired
    private ReservationService reservationService;

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext context) {
        if (value.length != 2 || !(value[0] instanceof String) || !(value[1] instanceof ReservationUpdateRequest)) {
            return false;
        }

        String reservationNumber = (String) value[0];
        ReservationUpdateRequest updateRequest = (ReservationUpdateRequest) value[1];

        // 予約番号のバリデーション
        if (reservationNumber == null || reservationNumber.length() != 26) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("26桁の予約番号を入力してください。")
                    .addConstraintViolation();
            return false;
        }

        // 予約の存在チェック
        try {
            reservationService.findByReservationNumber(reservationNumber);
        } catch (ReservationNotFoundException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("お探しの予約情報は存在しません。予約番号をご確認ください。")
                    .addConstraintViolation();
            return false;
        }

        // 更新リクエストのバリデーション
        if (updateRequest.getReservationDate() == null || updateRequest.getReservationTime() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("予約日時は必須です。")
                    .addConstraintViolation();
            return false;
        }

        // 予約時間のバリデーション
        LocalTime time = updateRequest.getReservationTime();
        if (time.getHour() < 11 || time.getHour() > 14 || (time.getMinute() != 0 && time.getMinute() != 30)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("予約時間は11:00から14:00までの間で、30分単位で選択してください。（例: 11:00、11:30）")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
