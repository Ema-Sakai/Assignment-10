package org.example.catcafereservation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReservationTimeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservationTime {
    String message() default "予約時間は11:00から14:00までの間で、30分単位で選択してください。（例: 11:00、11:30）";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
