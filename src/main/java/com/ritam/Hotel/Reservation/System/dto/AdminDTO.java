package com.ritam.Hotel.Reservation.System.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminDTO {

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Email can not be blank")
    private String email;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @NotBlank(message = "Phone number can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Address can not be blank")
    private String address;

    // Getters and Setters


    public @NotBlank(message = "Name can not be blank") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name can not be blank") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email can not be blank") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email can not be blank") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password can not be blank") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password can not be blank") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Phone number can not be blank") String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank(message = "Phone number can not be blank") String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Address can not be blank") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "Address can not be blank") String address) {
        this.address = address;
    }
}
