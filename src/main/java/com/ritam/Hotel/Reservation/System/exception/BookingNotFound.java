package com.ritam.Hotel.Reservation.System.exception;

public class BookingNotFound extends RuntimeException {
    public BookingNotFound(String message) {
        super(message);
    }
}
