package org.example.catcafereservation;

import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface ReservationMapper {

    @Select("SELECT r.id, r.name, r.reservation_date, r.reservation_time, r.email, r.phone, rn.reservation_number " +
            "FROM reservations r INNER JOIN reservations_numbers rn ON r.id = rn.reservation_id " +
            "WHERE rn.reservation_number = #{reservationNumber}")
    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Insert("INSERT INTO reservations (name, reservation_date, reservation_time, email, phone) VALUES (#{name}, #{reservationDate}, #{reservationTime}, #{email}, #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Reservation reservation);

    @Insert("INSERT INTO reservations_numbers (reservation_number, reservation_id) VALUES (#{reservationNumber}, #{id})")
    void insertReservationNumber(String reservationNumber, Integer id);

    @Update("UPDATE reservations SET reservation_date = #{reservationDate}, reservation_time = #{reservationTime} WHERE id = #{id}")
    void update(Reservation reservation);
}
