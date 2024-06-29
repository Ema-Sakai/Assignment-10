package org.example.catcafereservation;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface ReservationMapper {

    @Select("SELECT r.* FROM reservations r INNER JOIN reservations_numbers rn ON r.id = rn.reservation_id WHERE rn.reservation_number = #{reservationNumber}")
    Optional<Reservation> findByReservationNumber(String reservationNumber);

    @Insert("INSERT INTO reservations (name, reservation_date, reservation_time, email, phone) VALUES (#{name}, #{reservation_date}), #{reservation_time}), #{email}), #{phone})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Reservation reservation);
}
