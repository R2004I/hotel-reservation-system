package com.ritam.Hotel.Reservation.System.service.impl;

import com.ritam.Hotel.Reservation.System.dto.BookingDTO;
import com.ritam.Hotel.Reservation.System.entity.Booking;
import com.ritam.Hotel.Reservation.System.entity.Room;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.exception.BookingNotFound;
import com.ritam.Hotel.Reservation.System.exception.ResourceNotAvailableException;
import com.ritam.Hotel.Reservation.System.exception.RoomNotFound;
import com.ritam.Hotel.Reservation.System.exception.UserNotFound;
import com.ritam.Hotel.Reservation.System.helper.PaginationUtil;
import com.ritam.Hotel.Reservation.System.repository.BookingRepository;
import com.ritam.Hotel.Reservation.System.repository.RoomRepository;
import com.ritam.Hotel.Reservation.System.repository.UserRepository;
import com.ritam.Hotel.Reservation.System.service.BookingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepo;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PaginationUtil<Booking> paginationUtil;

    @Override
    @Transactional
    public Booking createReservation(String email, Long roomId, BookingDTO bookingDTO) {

        UserEntity existingUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User not found with email: " + email));
        Room existingRoom = roomRepo.findById(roomId)
                .orElseThrow(() -> new RoomNotFound("Room not found with ID: " + roomId));

        // Check if the room is available
        if (!existingRoom.isAvailable()) {
            throw new ResourceNotAvailableException("Room is not available for booking.");
        }

        // Validate booking dates
        if (bookingDTO.getCheckInDate().isAfter(bookingDTO.getCheckOutDate()) ||
                bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ResourceNotAvailableException("Invalid booking dates. Please check your check-in and check-out dates.");
        }

        // Create and set up a new booking
        Booking newBooking = new Booking();
        newBooking.setCheckInDate(bookingDTO.getCheckInDate());
        newBooking.setCheckOutDate(bookingDTO.getCheckOutDate());
        newBooking.setNumOfAdults(bookingDTO.getNumOfAdults());
        newBooking.setNumOfChildren(bookingDTO.getNumOfChildren());
        newBooking.setBookingConfirmationCode(null);

        // Establish relationships
        newBooking.setUser(existingUser);
        existingUser.getBookingList().add(newBooking);

        newBooking.setRoom(existingRoom);
        existingRoom.getBookings().add(newBooking);

        // Update room availability
        existingRoom.setAvailable(false);

        // Save to the database
        bookingRepo.save(newBooking);
        userRepo.save(existingUser);
        roomRepo.save(existingRoom);

        return newBooking;

    }

    @Override
    @Transactional
    public String cancelReservation(Long BookingId) {

        Booking existingBooking = bookingRepo.findById(BookingId)
                .orElseThrow(() -> new BookingNotFound("Reservation not found with ID: " + BookingId));

        if (existingBooking.getBookingStatus() == Booking.Status.CANCELLED) {
            throw new BookingNotFound("Reservation is already canceled.");
        }
        if (existingBooking.getCheckInDate().isBefore(LocalDate.now())) {
            throw new ResourceNotAvailableException("Cannot cancel a reservation after the check-in date.");
        }

        existingBooking.setBookingStatus(Booking.Status.CANCELLED);
        bookingRepo.save(existingBooking);

        // Restore room availability
        Room room = existingBooking.getRoom();
        room.setAvailable(true);
        roomRepo.save(room);

        return "Reservation with ID: " + BookingId + " has been successfully canceled.";
    }

    @Transactional
    @Override
    public String deleteReservation(Long bookingId) {
        Booking existingBooking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound("Reservation not found with ID: " + bookingId));

        bookingRepo.deleteByBookingId(bookingId);
        bookingRepo.deleteById(bookingId);

        // Restore room availability
        Room room = existingBooking.getRoom();
        room.setAvailable(true);
        roomRepo.save(room);

        return "Reservation with ID: " + bookingId + " has been successfully deleted.";
    }


    @Override
    public Booking getReservationById(Long BookingId) {
        return bookingRepo.findById(BookingId)
                .orElseThrow(() -> new BookingNotFound("Booking not found with ID: " + BookingId));
    }

    @Override
    public List<Booking> getReservationsByUser(Long userId) {
        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFound("User not found with ID: " + userId));

        // Return the user's booking list
        return user.getBookingList();
    }

    public List<Booking> getAllBookingsOfRoom(Long roomId){
        Room room = roomRepo.findById(roomId)
                .orElseThrow(()-> new RoomNotFound("Room not found with ID: " + roomId));
        return room.getBookings();
    }

    @Override
    public Page<Booking> getReservationsByRoom(Long roomId,Pageable pageable) {
       List<Booking> allBooking = getAllBookingsOfRoom(roomId);
       return paginationUtil.paginate(allBooking,pageable);
    }

    @Override
    public int calculateTotalPrice(Long BookingId) {
        Booking booking = bookingRepo.findById(BookingId)
                .orElseThrow(() -> new BookingNotFound("Booking not found with ID: " + BookingId));

        int numberOfNights = (int) ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());

        if (numberOfNights < 1) {
            throw new ResourceNotAvailableException("Invalid booking dates: Check-out date must be after check-in date.");
        }

        int pricePerNight = booking.getRoom().getRoomPrice();

        return numberOfNights * pricePerNight;
    }


}
