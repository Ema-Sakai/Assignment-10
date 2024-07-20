package org.example.catcafereservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Validated(ValidationOrder.class)
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Reservation> getReservation(@PathVariable @ValidReservationNumber @NotBlank String reservationNumber) {
        Reservation reservation = reservationService.findByReservationNumber(reservationNumber);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/")
    public ResponseEntity<ReservationResponse> insert(@Valid @RequestBody ReservationRequest reservationRequest, UriComponentsBuilder uriBuilder) {
        Reservation reservation = reservationService.insert(reservationRequest.convertToEntity());

        String reservationNumber = reservation.getReservationNumber();

        URI location = uriBuilder.path("/reservations/{reservationNumber}").buildAndExpand(reservationNumber).toUri();

        ReservationResponse body = new ReservationResponse(
                "以下の通り予約が完了しました。",
                reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                reservation.getReservationTime().format(DateTimeFormatter.ofPattern("HH時mm分")),
                reservation.getName(),
                reservation.getEmail(),
                reservation.getPhone(),
                reservationNumber
        );

        return ResponseEntity.created(location).body(body);
    }
}
