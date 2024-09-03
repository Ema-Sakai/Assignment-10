package org.example.catcafereservation;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReservationMapperTest {

    @Autowired
    private ReservationMapper reservationMapper;

    @Nested
    class ReadTests {

        @Test
        @DataSet(value = "datasets/reservations.yml")
        @Transactional
        void 指定した予約番号の予約情報を取得できること() {
            // Arrange
            String reservationNumber = "01J2K2JKM8Y8QES70ZQ0S73JSR";
            Reservation expected = new Reservation();
            expected.setId(1);
            expected.setName("名前はにゃんでも登録できちゃうにゃん太郎");
            expected.setReservationDate(LocalDate.of(2024, 9, 20));
            expected.setReservationTime(LocalTime.of(11, 30));
            expected.setEmail("test@example.com");
            expected.setPhone("02022222222");
            expected.setReservationNumber(reservationNumber);

            // Act
            Optional<Reservation> actual = reservationMapper.findByReservationNumber(reservationNumber);

            // Assert
            assertThat(actual).hasValue(expected);
        }

        @Test
        @DataSet(value = "datasets/reservations.yml")
        @Transactional
        void 存在しない予約番号を指定した場合に空のOptionalが返ること() {
            // Arrange
            String reservationNumber = "invalidReservationNumber";

            // Act
            Optional<Reservation> actual = reservationMapper.findByReservationNumber(reservationNumber);

            // Assert
            assertThat(actual).isEmpty();
        }
    }

    @Nested
    class CreateTests {

        @Test
        @DataSet(value = "datasets/reservations.yml")
        @ExpectedDataSet(value = "datasets/expected_reservations_after_create.yml", ignoreCols = {"id", "reservation_id"})
        @Transactional
        void 予約情報が登録できること() {
            // Arrange
            Reservation newReservation = new Reservation("新しい予約のにゃん太郎", LocalDate.of(2024, 10, 10), LocalTime.of(12, 0), "new@example.com", "09012345678");
            String reservationNumber = "03J2K1JKM8Y8QES70ZQ0S73JSR";

            // Act
            reservationMapper.insert(newReservation);
            reservationMapper.insertReservationNumber(reservationNumber, newReservation.getId());

            // Assert
            assertThat(newReservation.getId()).isNotNull().isGreaterThan(0);
            assertThat(reservationNumber).isNotNull().hasSize(26);

            // Check for uniqueness
            Optional<Reservation> duplicateReservation = reservationMapper.findByReservationNumber(reservationNumber);
            assertThat(duplicateReservation).isPresent();
            assertThat(duplicateReservation.get().getId()).isEqualTo(newReservation.getId());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        @DataSet(value = "datasets/reservations.yml", cleanAfter = true)
        @ExpectedDataSet(value = "datasets/expected_reservations_after_update.yml")
        @Transactional
        void 予約情報の更新ができること() {
            // Arrange
            Reservation updatedReservation = new Reservation(1, "名前はにゃんでも登録できちゃうにゃん太郎", LocalDate.of(2024, 12, 12), LocalTime.of(14, 0), "test@example.com", "02022222222", "01J2K2JKM8Y8QES70ZQ0S73JSR");

            // Act
            reservationMapper.update(updatedReservation);

            // Assert
            Optional<Reservation> actual = reservationMapper.findByReservationNumber("01J2K2JKM8Y8QES70ZQ0S73JSR");
            assertThat(actual).hasValueSatisfying(reservation -> {
                assertThat(reservation.getId()).isEqualTo(1);
                assertThat(reservation.getReservationNumber()).isEqualTo("01J2K2JKM8Y8QES70ZQ0S73JSR");
            });
        }
    }

    @Nested
    class DeleteTests {

        @Test
        @DataSet(value = "datasets/reservations.yml", cleanAfter = true)
        @ExpectedDataSet(value = "datasets/expected_reservations_after_delete.yml")
        @Transactional
        void 予約情報が削除されていること() {
            // Arrange
            String reservationNumber = "01J2K2JKM8Y8QES70ZQ0S73JSR";

            // Act
            reservationMapper.deleteReservationNumber(reservationNumber);
            reservationMapper.deleteReservation(1);

            // Assert
            Optional<Reservation> actual = reservationMapper.findByReservationNumber(reservationNumber);
            assertThat(actual).isEmpty();
        }
    }
}
