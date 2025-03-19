package com.ritam.Hotel.Reservation.System.dto;

import com.ritam.Hotel.Reservation.System.entity.Room;



public class RoomDTO {

    private String roomType;
    private int roomPrice;
    private String roomDescription;


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }



    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }


    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }
}
