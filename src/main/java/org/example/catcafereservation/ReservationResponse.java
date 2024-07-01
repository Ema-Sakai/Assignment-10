package org.example.catcafereservation;

//レコードクラスへ変換もありだが、クラス拡張の可能性などを考慮し保留。
public class ReservationResponse {

    private final String message;
    private final String reservationDate;
    private final String reservationTime;
    private final String name;
    private final String reservationNumber;

    public ReservationResponse(String message, String reservationDate, String reservationTime, String name, String reservationNumber) {
        this.message = message;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.name = name;
        this.reservationNumber = reservationNumber;
    }

    public String getMessage() {
        return message;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public String getName() {
        return name;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }
}
