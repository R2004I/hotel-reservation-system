package com.ritam.Hotel.Reservation.System.service;

import com.ritam.Hotel.Reservation.System.dto.HotelDTO;
import com.ritam.Hotel.Reservation.System.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface HotelService {

    Hotel addHotel(HotelDTO hotel, MultipartFile file) throws IOException;
    Hotel updateHotel(Long hotelId, Hotel updatedHotel);
    Hotel getHotelById(Long hotelId);
    Page<Hotel> getAllHotels(Integer page, Integer size);
    List<Hotel> searchHotels(String keyword);
    void deleteHotel(Long hotelId);
    List<String> getAllFeedbackOfHotel(long hotelId);
    String addFeedback(Long hotelId, String feedback);

}
