package com.ritam.Hotel.Reservation.System.service.impl;

import com.ritam.Hotel.Reservation.System.dto.HotelDTO;
import com.ritam.Hotel.Reservation.System.entity.Hotel;
import com.ritam.Hotel.Reservation.System.exception.HotelNotFound;
import com.ritam.Hotel.Reservation.System.repository.HotelRepository;
import com.ritam.Hotel.Reservation.System.service.HotelService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepo;

    @Override
    public Hotel addHotel(HotelDTO dto, MultipartFile imageFile) throws IOException, java.io.IOException {
        Hotel newHotel = new Hotel();

        newHotel.setName(dto.getName());
        newHotel.setDescription(dto.getDescription());
        newHotel.setContact(dto.getContact());
        newHotel.setAmenities(dto.getAmenities());
        newHotel.setLocation(dto.getLocation());

        newHotel.setImageName(imageFile.getOriginalFilename());
        newHotel.setImageType(imageFile.getContentType());
        newHotel.setImageData(imageFile.getBytes());


        hotelRepo.save(newHotel);
        return newHotel;
    }

    @Override
    public Hotel updateHotel(Long hotelId, Hotel updatedHotel) {
        Hotel existingHotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new HotelNotFound("Hotel with id " + hotelId + " not found"));

        if (updatedHotel.getName() != null) {
            existingHotel.setName(updatedHotel.getName());
        }
        if (updatedHotel.getLocation() != null) {
            existingHotel.setLocation(updatedHotel.getLocation());
        }
        if (updatedHotel.getAmenities() != null) {
            existingHotel.setAmenities(updatedHotel.getAmenities());
        }
        if (updatedHotel.getDescription() != null) {
            existingHotel.setDescription(updatedHotel.getDescription());
        }
        if (updatedHotel.getContact() != null) {
            existingHotel.setContact(updatedHotel.getContact());
        }

        hotelRepo.save(existingHotel);
        return existingHotel;
    }

    @Override
    public Hotel getHotelById(Long hotelId) {
        return hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel with id " + hotelId + " not found"));
    }

    @Override
    public Page<Hotel> getAllHotels(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepo.findAll(pageable);
    }

    @Override
    public List<Hotel> searchHotels(String keyword) {
        return hotelRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public void deleteHotel(Long hotelId) {
        Hotel existingHotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new HotelNotFound("Hotel with id " + hotelId + " not found"));
        hotelRepo.deleteById(hotelId);
        existingHotel.setRooms(null);
    }

   public List<String> getAllFeedbackOfHotel(long hotelId){
       Hotel hotel = hotelRepo.findById(hotelId)
               .orElseThrow(() -> new RuntimeException("Hotel with id " + hotelId + " not found"));
       return hotel.getRating();
   }

    @Override
    public String addFeedback(Long hotelId, String feedback) {
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel with id " + hotelId + " not found"));
        hotel.getRating().add(feedback);
        return "Feedback added successfully";
    }
}
