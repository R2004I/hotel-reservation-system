package com.ritam.Hotel.Reservation.System.dto;

import jakarta.validation.constraints.NotBlank;


public class CustomerDTO {

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "email can not be blank")
    private String email;

    @NotBlank(message = "password can not be blank")
    private String password;

    @NotBlank(message = "phone number can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Address can not be blank")
    private String address;

    //Getters and Setters

    public @NotBlank(message = "Name can not be blank") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name can not be blank") String name) {
        this.name = name;
    }

    public @NotBlank(message = "email can not be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "email can not be blank") String email) {
        this.email = email;
    }

    public @NotBlank(message = "password can not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password can not be blank") String password) {
        this.password = password;
    }

    public @NotBlank(message = "phone number can not be blank") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "phone number can not be blank") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Address can not be blank") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address can not be blank") String address) {
        this.address = address;
    }
}
