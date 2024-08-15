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

    @InjectMocks
    private ReservationService reservationService;

    @Test
    public void 存在する予約番号を指定したときに正常に予約情報が返されること() {
        String reservationNumber = "validReservationNumber123";
        Reservation expectedReservation = new Reservation(1, "Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678", reservationNumber);
        doReturn(Optional.of(expectedReservation)).when(reservationMapper).findByReservationNumber(reservationNumber);

        Reservation actualReservation = reservationService.findByReservationNumber(reservationNumber);

        assertThat(actualReservation).isEqualTo(expectedReservation);
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }

    @Test
    public void 存在しない予約番号を指定したときにエラーが返されること() {
        String reservationNumber = "invalidReservationNumber123";
        doReturn(Optional.empty()).when(reservationMapper).findByReservationNumber(reservationNumber);

        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(reservationNumber));
        verify(reservationMapper, times(1)).findByReservationNumber(reservationNumber);
    }

    @Test
    public void 予約番号がNullのときにエラーが返されること() {
        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(null));
    }

    @Test
    public void 予約番号が空文字のときにエラーが返されること() {
        assertThrows(ReservationNotFoundException.class, () -> reservationService.findByReservationNumber(""));
    }

    //ここまでがRead機能に対しての単体テスト

    //ここからがCreate機能に対しての単体テスト

    @Test
    public void 予約が正常に作成されること() {
        Reservation reservation = new Reservation("Test User", LocalDate.of(2024, 8, 7), LocalTime.of(12, 0), "test@example.com", "09012345678");
        doNothing().when(reservationMapper).insert(reservation);
        doNothing().when(reservationMapper).insertReservationNumber(anyString(), eq(reservation.getId()));

        Reservation createdReservation = reservationService.insert(reservation);

        assertThat(createdReservation).isNotNull();
        assertThat(createdReservation.getReservationNumber()).isNotNull();
        verify(reservationMapper, times(1)).insert(reservation);
        verify(reservationMapper, times(1)).insertReservationNumber(anyString(), eq(reservation.getId()));
    }

    //ここまでがCreate機能に対しての単体テスト

}
