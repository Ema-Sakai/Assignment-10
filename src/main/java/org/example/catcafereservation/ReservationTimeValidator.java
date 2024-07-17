package org.example.catcafereservation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class ReservationTimeValidator implements ConstraintValidator<ValidReservationTime, LocalTime> {
    @Override
    public boolean isValid(LocalTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int hour = value.getHour();
        int minute = value.getMinute();
        return (hour >= 11 && hour <= 14) && (minute == 0 || minute == 30);
    }

}
