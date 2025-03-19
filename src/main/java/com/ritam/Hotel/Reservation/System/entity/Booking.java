package com.ritam.Hotel.Reservation.System.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "check in date is required")
    private LocalDate checkInDate;

    @Future(message = "check out date must be in the future")
    private LocalDate checkOutDate;

    @Min(value = 1, message = "Number of adults must not be less that 1")
    private int numOfAdults;

    @Min(value = 0, message = "Number of children must not be less that 0")
    private int numOfChildren;
    @Enumerated(EnumType.STRING)  // Ensure correct ENUM mapping
    @Column(length = 20) // Ensure it has enough space
    private Status bookingStatus;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    public enum Status{
        CONFIRMED,CANCELLED
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull(message = "check in date is required") LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(@NotNull(message = "check in date is required") LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public @Future(message = "check out date must be in the future") LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(@Future(message = "check out date must be in the future") LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Min(value = 1, message = "Number of adults must not be less that 1")
    public int getNumOfAdults() {
        return numOfAdults;
    }

    public void setNumOfAdults(@Min(value = 1, message = "Number of adults must not be less that 1") int numOfAdults) {
        this.numOfAdults = numOfAdults;
    }

    @Min(value = 0, message = "Number of children must not be less that 0")
    public int getNumOfChildren() {
        return numOfChildren;
    }

    public void setNumOfChildren(@Min(value = 0, message = "Number of children must not be less that 0") int numOfChildren) {
        this.numOfChildren = numOfChildren;
    }

    public Status getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(Status bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingConfirmationCode() {
        return bookingConfirmationCode;
    }

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
