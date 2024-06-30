package org.example.catcafereservation;

public class ReservationResponse {

    private String message;

    public ReservationResponse(String message, String yyyy年MM月dd日, String hh時mm分, String name, String reservationNumber) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
