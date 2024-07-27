package org.example.catcafereservation;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ReservationValidator {

    private final MessageSource messageSource;

    public ReservationValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void validateReservationNumber(String reservationNumber) {
        if (Objects.isNull(reservationNumber) || reservationNumber.length() != 26) {
            throw new ReservationUpdateValidationException(getMessage("reservation.number.invalid"));
        }
    }

    public void validateReservationUpdate(LocalDate currentReservationDate, LocalDate newReservationDate, LocalTime newReservationTime) {
        Map<String, String> errors = new HashMap<>();

        if (currentReservationDate.isBefore(LocalDate.now())) {
            throw new ReservationUpdateValidationException(getMessage("reservation.update.past"));
        }

        if (Objects.isNull(newReservationDate) || !newReservationDate.isAfter(LocalDate.now())) {
            errors.put("reservationDate", getMessage("reservation.date.future"));
        }

        if (Objects.isNull(newReservationTime) || newReservationTime.getHour() < 11 || newReservationTime.getHour() > 14 || (newReservationTime.getMinute() != 0 && newReservationTime.getMinute() != 30)) {
            errors.put("reservationTime", getMessage("reservation.time.invalid"));
        }

        if (!errors.isEmpty()) {
            throw new ReservationUpdateValidationException(getMessage("reservation.update.invalid"), errors);
        }
    }

    public void validateNoChanges(LocalDate currentReservationDate, LocalTime currentReservationTime, LocalDate newReservationDate, LocalTime newReservationTime) {
        if (newReservationDate.equals(currentReservationDate) && newReservationTime.equals(currentReservationTime)) {
            throw new ReservationUpdateValidationException(getMessage("reservation.update.nochange"));
        }
    }

    private String getMessage(String code) {
        if (Objects.isNull(code)) {
            return "";
        }
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
