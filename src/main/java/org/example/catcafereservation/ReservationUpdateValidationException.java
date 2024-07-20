package org.example.catcafereservation;

import java.util.Map;

public class ReservationUpdateValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ReservationUpdateValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
