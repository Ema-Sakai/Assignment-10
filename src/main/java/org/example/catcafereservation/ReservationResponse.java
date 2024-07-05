package org.example.catcafereservation;

import lombok.Getter;

//レコードクラスへ変換もありだが、クラス拡張の可能性などを考慮し保留。
@Getter
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

}
