package org.example.catcafereservation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationValidator reservationValidator;

    @InjectMocks
    private ReservationService reservationService;

    @Nested
    class ReadTests {

        @Test
        void 存在する予約番号を指定したときに正常に予約情報が返されること() {
            // Arrange
            String reservationNumber = "validReservationNumber123";
            Reservation expectedReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.of(expectedReservation));

            // Act
            Reservation actualReservation = reservationService.findByReservationNumber(reservationNumber);

            // Assert
            assertThat(actualReservation).isEqualTo(expectedReservation);

            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        }

        @Test
        void 存在しない予約番号を指定したときにエラーが返されること() {
            // Arrange
            String reservationNumber = "invalidReservationNumber123";
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(reservationNumber));

            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        void 無効な予約番号でエラーが返されること(String invalidReservationNumber) {
            // Act & Assert
            assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(invalidReservationNumber));
        }
    }

    @Nested
    class CreateTests {

        @Test
        void 予約情報が正常に作成されること() {
            // Arrange
            Reservation reservation = new Reservation("Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678");
            String expectedReservationNumber = "generatedReservationNumber";

            // Act
            Reservation createdReservation = reservationService.insert(reservation);

            // Assert
            assertThat(createdReservation).isNotNull();
            assertThat(createdReservation.getReservationNumber()).isNotNull();
            assertThat(createdReservation.getReservationNumber().length()).isEqualTo(26);

            assertThat(createdReservation)
                    .usingRecursiveComparison()
                    .ignoringFields("reservationNumber")
                    .isEqualTo(reservation);

            verify(reservationMapper, times(1)).insert(reservation);
            verify(reservationMapper, times(1)).insertReservationNumber(anyString(), eq(reservation.getId()));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void 予約情報が正常に更新されること() {
            // Arrange
            String reservationNumber = "validReservationNumber123";
            Reservation existingReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
            LocalDate newReservationDate = LocalDate.of(2024, 8, 8);
            LocalTime newReservationTime = LocalTime.of(13, 0);
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.of(existingReservation));

            // Act
            Reservation updatedReservation = reservationService.updateReservation(reservationNumber, newReservationDate, newReservationTime);

            // Assert
            assertThat(updatedReservation.getReservationDate()).isEqualTo(newReservationDate);
            assertThat(updatedReservation.getReservationTime()).isEqualTo(newReservationTime);
            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
            verify(reservationMapper, times(1)).update(existingReservation);
            verify(reservationValidator, times(1)).validateReservationNumber(reservationNumber);
            verify(reservationValidator, times(1)).validateReservationUpdate(any(), any(), any());
            verify(reservationValidator, times(1)).validateNoChanges(any(), any(), any(), any());
        }

        @Test
        void 存在しない予約情報を更新しようとしたときにエラーが返されること() {
            // Arrange
            String reservationNumber = "invalidReservationNumber123";
            LocalDate newReservationDate = LocalDate.of(2024, 8, 8);
            LocalTime newReservationTime = LocalTime.of(13, 0);
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ReservationNotFoundException.class, () -> reservationService.updateReservation(reservationNumber, newReservationDate, newReservationTime));
            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void 予約番号で削除メソッドが呼び出されること() {
            // Arrange
            String reservationNumber = "validReservationNumber123";
            Reservation existingReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.of(existingReservation));

            // Act
            reservationService.deleteReservation(reservationNumber);

            // Assert
            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
            verify(reservationMapper, times(1)).deleteReservationNumber(reservationNumber);
            verify(reservationMapper, times(1)).deleteReservation(existingReservation.getId());
        }

        @Test
        void 存在しない予約番号を削除しようとしたときにエラーが返されること() {
            // Arrange
            String reservationNumber = "invalidReservationNumber123";
            when(reservationMapper.findByReservationNumber(reservationNumber)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteReservation(reservationNumber));

            verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        }
    }
}

