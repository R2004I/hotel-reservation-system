package com.ritam.Hotel.Reservation.System.service;

import com.ritam.Hotel.Reservation.System.dto.BookingDTO;
import com.ritam.Hotel.Reservation.System.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public interface BookingService {

    @Transactional
    Booking createReservation(String email, Long roomId, BookingDTO bookingDTO);
    @Transactional
    String cancelReservation(Long BookingId);
    @Transactional
    String deleteReservation(Long bookingId);
    Booking getReservationById(Long BookingId);
    List<Booking> getReservationsByUser(Long userId);
    Page<Booking> getReservationsByRoom(Long roomId, Pageable pageable);
    int calculateTotalPrice(Long BookingId);





}
