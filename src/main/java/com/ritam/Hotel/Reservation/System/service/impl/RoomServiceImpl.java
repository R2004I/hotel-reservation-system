package com.ritam.Hotel.Reservation.System.service.impl;

import com.ritam.Hotel.Reservation.System.dto.RoomDTO;
import com.ritam.Hotel.Reservation.System.entity.Booking;
import com.ritam.Hotel.Reservation.System.entity.Hotel;
import com.ritam.Hotel.Reservation.System.entity.Room;
import com.ritam.Hotel.Reservation.System.exception.HotelNotFound;
import com.ritam.Hotel.Reservation.System.repository.BookingRepository;
import com.ritam.Hotel.Reservation.System.repository.HotelRepository;
import com.ritam.Hotel.Reservation.System.repository.RoomRepository;
import com.ritam.Hotel.Reservation.System.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private HotelRepository hotelRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Override
    public Room addRoomToHotel(Long hotelId, RoomDTO room, MultipartFile imageFile) throws IOException {
        Hotel existingHotel = hotelRepo.findById(hotelId).orElseThrow(() -> new RuntimeException("Hotel with id " + hotelId + " not found"));
        Room newroom = new Room();
        newroom.setRoomPrice(room.getRoomPrice());
        newroom.setImageName(imageFile.getOriginalFilename());
        newroom.setImageType(imageFile.getContentType());
        newroom.setImageData(imageFile.getBytes());
        newroom.setRoomType(room.getRoomType());
        newroom.setRoomDescription(room.getRoomDescription());
        newroom.setAvailable(true);
        newroom.setHotel(existingHotel);
        Room savedRoom = roomRepo.save(newroom);
        existingHotel.getRooms().add(savedRoom);
        return savedRoom;
    }

    @Override
    public Room updateRoom(Long roomId, Room updatedRoom, MultipartFile imageFile) throws IOException {
        Room existingRoom = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " not found"));
        if (updatedRoom.getRoomType() != null) {
            existingRoom.setRoomType(updatedRoom.getRoomType());
        }
        if (updatedRoom.getRoomPrice() > 0) {
            existingRoom.setRoomPrice(updatedRoom.getRoomPrice());
        }
        if (updatedRoom.getImageData() != null && updatedRoom.getImageName() != null && updatedRoom.getImageType() != null) {
            existingRoom.setImageName(imageFile.getOriginalFilename());
            existingRoom.setImageType(imageFile.getContentType());
            existingRoom.setImageData(imageFile.getBytes());
        }
        if (updatedRoom.getRoomDescription() != null && !updatedRoom.getRoomDescription().isEmpty()) {
            existingRoom.setRoomDescription(updatedRoom.getRoomDescription());
        }
        existingRoom.setAvailable(updatedRoom.isAvailable());
        return roomRepo.save(existingRoom);
    }

    @Override
    public Room getRoomById(Long roomId) {
        return roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " not found"));
    }

    @Override
    public Page<Room> getAllRoomsByHotel(Long hotelId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepo.getAllRooms(hotelId, pageable);
    }

    @Override
    public boolean checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " not found"));
        if (!room.isAvailable()) {
            return false;
        }
        for (Booking booking : room.getBookings()) {
            LocalDate existingCheckIn = booking.getCheckInDate();
            LocalDate existingCheckOut = booking.getCheckOutDate();
            if ((checkIn.isBefore(existingCheckOut) && checkOut.isAfter(existingCheckIn)) ||
                    (checkIn.isEqual(existingCheckIn) || checkOut.isEqual(existingCheckOut))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " not found"));
        List<Booking> bookings = room.getBookings();
        for (Booking booking : bookings) {
            booking.setBookingStatus(Booking.Status.CANCELLED);
            bookingRepo.save(booking);
        }
        Hotel hotel = room.getHotel();
        hotel.getRooms().remove(room);
        roomRepo.delete(room);
    }

    @Override
    public Page<Room> searchRooms(Long hotelId, String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepo.findByNameContainingIgnoreCase(hotelId,pageable,keyword);
    }

    @Override
    public String addFeedback(Long roomId,String feedback) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room with ID " + roomId + " not found"));
        room.getFeedback().add(feedback);
        roomRepo.save(room);
        return "Feedback added successfully";
    }

    @Override
    public Page<Room> getAllAvailableRoom(Long hotelId, Integer page, Integer size) {
        hotelRepo.findById(hotelId).orElseThrow(() -> new HotelNotFound("Hotel with id " + hotelId + " not found"));
        Pageable pageable = PageRequest.of(page, size);
        return roomRepo.getAllAvailableRooms(hotelId, pageable);
    }

}
