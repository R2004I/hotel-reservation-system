package com.ritam.Hotel.Reservation.System.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class BookingDTO {


    @NotNull(message = "check in date is required")
    private LocalDate checkInDate;

    @Future(message = "check out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of adults must not be less that 1")
    private int numOfAdults;

    @Min(value = 0, message = "Number of children must not be less that 0")
    private int numOfChildren;

    //Getters and Setters
    public @NotNull(message = "check in date is required") LocalDate getCheckInDate() {
        return checkInDate;
    }

    public @Future(message = "check out date must be in the future") LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    @Min(value = 1, message = "Number of adults must not be less that 1")
    public int getNumOfAdults() {
        return numOfAdults;
    }

    @Min(value = 0, message = "Number of children must not be less that 0")
    public int getNumOfChildren() {
        return numOfChildren;
    }
}
