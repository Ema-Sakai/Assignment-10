package org.example.catcafereservation;

import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface ReservationMapper {

    @Select("SELECT r.id, r.name, r.reservation_date, r.reservation_time, r.email, r.phone, rn.reservation_number " +
            "FROM reservations r INNER JOIN reservations_numbers rn ON r.id = rn.reservation_id " +
            "WHERE rn.reservation_number = #{reservationNumber}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "reservationDate", column = "reservation_date"),
            @Result(property = "reservationTime", column = "reservation_time"),
            @Result(property = "email", column = "email"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "reservationNumber", column = "reservation_number")
    })
    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Insert("INSERT INTO reservations (name, reservation_date, reservation_time, email, phone) VALUES (#{name}, #{reservationDate}, #{reservationTime}, #{email}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Reservation reservation);

    @Insert("INSERT INTO reservations_numbers (reservation_number, reservation_id) VALUES (#{reservationNumber}, #{id})")
    void insertReservationNumber(String reservationNumber, Integer id);
}
