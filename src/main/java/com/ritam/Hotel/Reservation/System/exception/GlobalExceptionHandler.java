package com.ritam.Hotel.Reservation.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(BookingNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(HotelNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(HotelNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(PaymentNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(RoomNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(RoomNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(UserNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotAvailableException.class)
    public ResponseEntity<String> handleResourceNotAvailableException(ResourceNotAvailableException ex)
    {
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FeedbackNotFound.class)
    public ResponseEntity<String> handleResourceNotFoundException(FeedbackNotFound ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}
