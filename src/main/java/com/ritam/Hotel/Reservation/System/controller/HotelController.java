package com.ritam.Hotel.Reservation.System.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ritam.Hotel.Reservation.System.dto.HotelDTO;
import com.ritam.Hotel.Reservation.System.entity.Hotel;
import com.ritam.Hotel.Reservation.System.security.JwtConfig;
import com.ritam.Hotel.Reservation.System.service.HotelService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("hotel")
public class HotelController {

    private final HotelService hotelService;

    private final JwtConfig jwt;

    private final ObjectMapper mapper;

    public HotelController(HotelService hotelService, JwtConfig jwt, ObjectMapper mapper) {
        this.hotelService = hotelService;
        this.jwt=jwt;
        this.mapper = mapper;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Hotel> addHotel(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam("hotelData") String hotel,
            @RequestParam("image") MultipartFile imageFile ) throws IOException
    {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                HotelDTO hotelDTO = mapper.readValue(hotel,HotelDTO.class);
                Hotel newHotel = hotelService.addHotel(hotelDTO,imageFile);
                return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
        {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @PutMapping("/update/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long hotelId,
            @RequestBody Hotel updatedHotel)
    {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                Hotel updated = hotelService.updateHotel(hotelId, updatedHotel);
                return ResponseEntity.ok(updated);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
        {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/all/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long hotelId) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotel);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllHotels(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<Hotel> hotels = hotelService.getAllHotels(page,size);
        return ResponseEntity.ok(hotels);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{hotelId}")
    public ResponseEntity<String> deleteHotel(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long hotelId)
    {
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                hotelService.deleteHotel(hotelId);
                return ResponseEntity.ok("Hotel with ID " + hotelId + " has been deleted successfully.");
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else
        {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @GetMapping("/all/search")
    public ResponseEntity<?> searchHotels(@RequestParam String keyword){
        List<Hotel> hotels = hotelService.searchHotels(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(hotels);
    }

    @PostMapping("/add-feedback")
    public ResponseEntity<?> addFeedback(@RequestParam("feedback") String feedback,
        @RequestParam("hotelId") Long hotelId)
    {
        String s = hotelService.addFeedback(hotelId, feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @GetMapping("/{hotelId}/image")
    public ResponseEntity<byte[]> getImageData(@PathVariable Long hotelId){
        Hotel hotelById = hotelService.getHotelById(hotelId);
        byte[] imageData = hotelById.getImageData();
        return ResponseEntity.status(HttpStatus.CREATED).body(imageData);
    }

}
