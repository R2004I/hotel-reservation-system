package com.ritam.Hotel.Reservation.System.controller;

import com.ritam.Hotel.Reservation.System.dto.BookingDTO;
import com.ritam.Hotel.Reservation.System.entity.Booking;
import com.ritam.Hotel.Reservation.System.security.JwtConfig;
import com.ritam.Hotel.Reservation.System.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("booking/")
public class BookingController {

      private final BookingService bookingService;

      private final JwtConfig jwt;

      @Autowired
    public BookingController(BookingService bookingService, JwtConfig jwt) {
        this.bookingService = bookingService;
        this.jwt = jwt;
    }

    @PostMapping("/user/{email}/room/{roomId}")
    public ResponseEntity<?> createReservation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable String email,
            @PathVariable Long roomId,
            @RequestBody BookingDTO bookingDTO)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                Booking newBooking = bookingService.createReservation(email, roomId, bookingDTO);
                Map<String, Object> response = new HashMap<>();
                response.put("bookingId", newBooking.getId());
                response.put("customerName",newBooking.getUser().getName());
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<String> cancelReservation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long bookingId)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                String message = bookingService.cancelReservation(bookingId);
                return ResponseEntity.ok(message);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @DeleteMapping("delete/{bookingId}")
    public ResponseEntity<String> DeleteReservation(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long bookingId)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                String message = bookingService.deleteReservation(bookingId);
                return ResponseEntity.ok(message);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getReservationById(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long bookingId)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                Booking booking = bookingService.getReservationById(bookingId);
                return ResponseEntity.ok(booking);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getReservationsByUser(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long userId)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                List<Booking> bookings = bookingService.getReservationsByUser(userId);
                return ResponseEntity.ok(bookings);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getReservationsByRoom(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long roomId,
            @RequestParam("page") Integer page, @RequestParam("size") Integer size)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                Pageable pageable = Pageable.ofSize(size).withPage(page);
                return ResponseEntity.ok(bookingService.getReservationsByRoom(roomId,pageable));
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/{bookingId}/price")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable Long bookingId) {
        double totalPrice = bookingService.calculateTotalPrice(bookingId);
        return ResponseEntity.ok(totalPrice);
    }
}
