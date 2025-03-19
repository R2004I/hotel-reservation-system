package com.ritam.Hotel.Reservation.System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotBlank(message = "Email can not be blank")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @NotNull(message = "Role can not be null")
    private String role;

    @NotBlank(message = "Phone number can not be blank")
    private String phoneNumber;

    @NotBlank(message = "Address can not be blank")
    private String address;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingList = new ArrayList<>();

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public @NotNull(message = "Role can not be null") String getRole() {
        return role;
    }

    public void setRole(@NotNull(message = "Role can not be null") String role) {
        this.role = role;
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

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
