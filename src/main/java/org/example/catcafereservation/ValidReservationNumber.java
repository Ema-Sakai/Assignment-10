package org.example.catcafereservation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReservationNumberValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservationNumber {
    String message() default "26桁の予約番号を入力してください。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
