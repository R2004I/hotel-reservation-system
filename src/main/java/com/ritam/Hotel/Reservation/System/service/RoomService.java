package com.ritam.Hotel.Reservation.System.service;

import com.ritam.Hotel.Reservation.System.dto.RoomDTO;
import com.ritam.Hotel.Reservation.System.entity.Room;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface RoomService {

    Room addRoomToHotel(Long hotelId, RoomDTO room, MultipartFile imageFile) throws IOException;
    Room updateRoom(Long roomId, Room updatedRoom, MultipartFile imageFile) throws IOException;
    Room getRoomById(Long roomId);
    Page<Room> getAllRoomsByHotel(Long hotelId, Integer page, Integer size);
    boolean checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut);
    void deleteRoom(Long roomId);
    Page<Room> getAllAvailableRoom(Long hotelId,Integer page, Integer size);
    Page<Room> searchRooms(Long hotelId, String keyword, Integer page, Integer size);

    String addFeedback(Long roomId, String feedback);


}
