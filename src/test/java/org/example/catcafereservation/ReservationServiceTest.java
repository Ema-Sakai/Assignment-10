package org.example.catcafereservation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class ReservationServiceTest {

    @Mock
    private ReservationMapper reservationMapper;

    @Mock
    private ReservationValidator reservationValidator;

    @InjectMocks
    private ReservationService reservationService;

    //Read機能に対しての単体テスト
    @Test
    public void findByReservationNumber_存在する予約番号を指定したときに正常に予約情報が返されること() {
        // Arrange
        String reservationNumber = "validReservationNumber123";
        Reservation expectedReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
        doReturn(Optional.of(expectedReservation)).when(reservationMapper).findByReservationNumber(reservationNumber);

        // Act
        Reservation actualReservation = reservationService.findByReservationNumber(reservationNumber);

        // Assert
        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }

    @Test
    public void findByReservationNumber_存在しない予約番号を指定したときにエラーが返されること() {
        // Arrange
        String reservationNumber = "invalidReservationNumber123";
        doReturn(Optional.empty()).when(reservationMapper).findByReservationNumber(reservationNumber);

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(reservationNumber));
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }

    @Test
    public void 予約番号がNullのときにエラーが返されること() {
        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(null));
    }

    @Test
    public void 予約番号が空文字のときにエラーが返されること() {
        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(""));
    }


    //Create機能に対しての単体テスト
    @Test
    public void insert_予約が正常に作成されること() {
        // Arrange
        Reservation reservation = new Reservation("Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678");
        doNothing().when(reservationMapper).insert(reservation);
        doNothing().when(reservationMapper).insertReservationNumber(anyString(), eq(reservation.getId()));

        // Act
        Reservation createdReservation = reservationService.insert(reservation);

        // Assert
        assertThat(createdReservation).isNotNull();
        assertThat(createdReservation.getReservationNumber()).isNotNull();
        verify(reservationMapper, times(1)).insert(reservation);
        verify(reservationMapper, times(1)).insertReservationNumber(anyString(), eq(reservation.getId()));
    }


    //Update機能に対しての単体テスト
    @Test
    public void updateReservation_予約情報が正常に更新されること() {
        // Arrange
        String reservationNumber = "validReservationNumber123";
        Reservation existingReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2024, 8, 8), LocalTime.of(13, 0));
        doReturn(Optional.of(existingReservation)).when(reservationMapper).findByReservationNumber(reservationNumber);
        doNothing().when(reservationValidator).validateReservationNumber(reservationNumber);
        doNothing().when(reservationValidator).validateReservationUpdate(any(), any(), any());
        doNothing().when(reservationValidator).validateNoChanges(any(), any(), any(), any());
        doNothing().when(reservationMapper).update(existingReservation);

        // Act
        Reservation updatedReservation = reservationService.updateReservation(reservationNumber, updateRequest);

        // Assert
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.getReservationDate()).isEqualTo(updateRequest.getReservationDate());
        assertThat(updatedReservation.getReservationTime()).isEqualTo(updateRequest.getReservationTime());
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        verify(reservationMapper, times(1)).update(existingReservation);
    }

    @Test
    public void updateReservation_存在しない予約情報を更新しようとしたときにエラーが返されること() {
        // Arrange
        String reservationNumber = "invalidReservationNumber123";
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.of(2024, 8, 8), LocalTime.of(13, 0));
        doReturn(Optional.empty()).when(reservationMapper).findByReservationNumber(reservationNumber);

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.updateReservation(reservationNumber, updateRequest));
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }

    //Delete機能に対しての単体テスト
    @Test
    public void deleteReservation_予約が正常に削除されること() {
        // Arrange
        String reservationNumber = "validReservationNumber123";
        Reservation existingReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
        doReturn(Optional.of(existingReservation)).when(reservationMapper).findByReservationNumber(reservationNumber);
        doNothing().when(reservationMapper).deleteReservationNumber(reservationNumber);
        doNothing().when(reservationMapper).deleteReservation(existingReservation.getId());

        // Act
        reservationService.deleteReservation(reservationNumber);

        // Assert
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
        verify(reservationMapper, times(1)).deleteReservationNumber(reservationNumber);
        verify(reservationMapper, times(1)).deleteReservation(existingReservation.getId());
    }

    @Test
    public void deleteReservation_存在しない予約番号を削除しようとしたときにエラーが返されること() {
        // Arrange
        String reservationNumber = "invalidReservationNumber123";
        doReturn(Optional.empty()).when(reservationMapper).findByReservationNumber(reservationNumber);

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.deleteReservation(reservationNumber));
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }
}
