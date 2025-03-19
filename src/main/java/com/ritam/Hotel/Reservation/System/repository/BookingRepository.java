package com.ritam.Hotel.Reservation.System.repository;

import com.ritam.Hotel.Reservation.System.entity.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Payment p WHERE p.booking.id = :bookingId")
    void deleteByBookingId(@Param("bookingId") Long bookingId);
}
