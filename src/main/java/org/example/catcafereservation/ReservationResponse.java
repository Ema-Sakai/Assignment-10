package org.example.catcafereservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

//レコードクラスへ変換もありだが、クラス拡張の可能性などを考慮し保留。
@Getter
@AllArgsConstructor
public class ReservationResponse {

    private final String message;
    private final String reservationDate;
    private final String reservationTime;
    private final String name;
    private final String email;
    private final String phone;
    private final String reservationNumber;

}
