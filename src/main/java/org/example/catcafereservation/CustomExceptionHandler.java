package org.example.catcafereservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = ReservationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReservationNotFoundException(
            ReservationNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", ZonedDateTime.now().toString());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        body.put("message", ex.getMessage());
        body.put("nextSteps", "予約番号が正しいことを確認してください。問題が解決しない場合は、カスタマーサポートまでお問い合わせください。");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
}
