package com.ritam.Hotel.Reservation.System.repository;

import com.ritam.Hotel.Reservation.System.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {

    List<Hotel> findByNameContainingIgnoreCase(String name);
}
