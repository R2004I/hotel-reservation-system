package com.ritam.Hotel.Reservation.System.repository;

import com.ritam.Hotel.Reservation.System.entity.Hotel;
import com.ritam.Hotel.Reservation.System.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

   Optional<Room> findByImageName(String imageName);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId AND r.isAvailable = true")
    Page<Room> getAllAvailableRooms(Long hotelId, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    Page<Room> getAllRooms(Long hotelId, Pageable pageable);

    @Query("SELECT r FROM Room r WHERE r.hotel.hotelId = :hotelId")
    Page<Room> findByNameContainingIgnoreCase(Long hotelId,Pageable pageable, String name);
}
