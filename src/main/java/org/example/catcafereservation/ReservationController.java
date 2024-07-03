package org.example.catcafereservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{reservationNumber}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String reservationNumber) {
        Reservation reservation = reservationService.findByReservationNumber(reservationNumber);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/")
    public ResponseEntity<ReservationResponse> insert(@RequestBody ReservationRequest reservationRequest, UriComponentsBuilder uriBuilder) {
        Reservation reservation = reservationService.insert(
                reservationRequest.getName(),
                reservationRequest.getReservationDate(),
                reservationRequest.getReservationTime(),
                reservationRequest.getEmail(),
                reservationRequest.getPhone()
        );

        String reservationNumber = reservationService.generateReservationNumber(reservation.getReservationDate(), reservation.getId());

        URI location = uriBuilder.path("/reservations/{reservation_number}").buildAndExpand(reservationNumber).toUri();

        ReservationResponse body = new ReservationResponse(
                "以下の通り予約が完了しました。",
                reservation.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")),
                reservation.getReservationTime().format(DateTimeFormatter.ofPattern("HH時mm分")),
                reservation.getName(),
                reservationNumber
        );

        return ResponseEntity.created(location).body(body);
    }
}
