package org.example.catcafereservation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReservationUpdateValidator.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidReservationUpdate {
    String message() default "予約更新リクエストが無効です。";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
