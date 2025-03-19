package com.ritam.Hotel.Reservation.System.exception;

public class RoomNotFound extends RuntimeException {
    public RoomNotFound(String message) {
        super(message);
    }
}
