package com.ritam.Hotel.Reservation.System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritam.Hotel.Reservation.System.dto.RoomDTO;
import com.ritam.Hotel.Reservation.System.entity.Room;
import com.ritam.Hotel.Reservation.System.security.JwtConfig;
import com.ritam.Hotel.Reservation.System.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    private final ObjectMapper mapper;

    private final JwtConfig jwt;

    public RoomController(RoomService roomService, ObjectMapper mapper, JwtConfig jwt) {
        this.roomService = roomService;
        this.mapper = mapper;
        this.jwt = jwt;
    }

    @PostMapping("/add/{hotelId}")
    public ResponseEntity<Room> addRoomToHotel(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long hotelId,
            @RequestParam("roomData") String room,
            @RequestParam("image") MultipartFile imageFile) throws IOException
    {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                RoomDTO roomDTO = mapper.readValue(room,RoomDTO.class);
                Room newRoom = roomService.addRoomToHotel(hotelId, roomDTO, imageFile);
                return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
            }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long roomId,
            @RequestPart("room") Room updatedRoom,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Room room = roomService.updateRoom(roomId, updatedRoom, imageFile);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long roomId) {
        Room room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<?> getAllRoomsByHotel(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @PathVariable Long hotelId) {
        Page<Room> rooms = roomService.getAllRoomsByHotel(hotelId,page,size);
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @PathVariable Long roomId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        boolean isAvailable = roomService.checkRoomAvailability(roomId, checkIn, checkOut);
        return ResponseEntity.ok(isAvailable);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteRoom(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long roomId)
    {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                roomService.deleteRoom(roomId);
                return ResponseEntity.ok("Room with ID " + roomId + " has been deleted successfully.");
            }else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }










    }

    @GetMapping("/hotel/{hotelId}/available")
    public ResponseEntity<?> getAllAvailableRooms(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @PathVariable Long hotelId) {
        Page<Room> availableRooms = roomService.getAllAvailableRoom(hotelId,page,size);
        return ResponseEntity.status(HttpStatus.OK).body(availableRooms);
    }

    @PostMapping("/add-feedback")
    public ResponseEntity<?> addFeedback(
            @RequestParam("roomId") Long roomId,
            @RequestParam("feedback") String feedback
    ){
        String s = roomService.addFeedback(roomId, feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @GetMapping("/{roomId}/image")
    public ResponseEntity<byte[]> getImageData(@PathVariable Long roomId){
        Room roomById = roomService.getRoomById(roomId);
        byte[] imageData = roomById.getImageData();
        return ResponseEntity.status(HttpStatus.CREATED).body(imageData);
    }

}
