package org.example.catcafereservation;

import lombok.Getter;

import java.util.Map;

@Getter
public class ReservationUpdateValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ReservationUpdateValidationException(String message) {
        super(message);
        this.errors = null;
    }

    public ReservationUpdateValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

}
