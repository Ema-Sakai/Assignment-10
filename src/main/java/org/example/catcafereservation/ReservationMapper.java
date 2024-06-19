package org.example.catcafereservation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ReservationMapper {

    @Select("SELECT r.* FROM reservations r INNER JOIN reservations_numbers rn ON r.id = rn.reservation_id WHERE rn.reservation_number = #{reservationNumber}")
    Optional<Reservation> findByReservationNumber(String reservationNumber);
}
