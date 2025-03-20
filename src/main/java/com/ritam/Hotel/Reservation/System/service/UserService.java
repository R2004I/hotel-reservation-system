package com.ritam.Hotel.Reservation.System.service;

import com.ritam.Hotel.Reservation.System.dto.AdminDTO;
import com.ritam.Hotel.Reservation.System.dto.CustomerDTO;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.exception.UserNotFound;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {

    public UserEntity getUserByEmailId(String emailId) throws UserNotFound;
    public UserEntity addUser(CustomerDTO customer) throws UserNotFound;
    public UserEntity addUserAdmin(AdminDTO admin	) throws UserNotFound;
    public UserEntity changePassword(Long userId, String newPassword) throws UserNotFound;
    public UserEntity getUserDetails(Long userId) throws UserNotFound;
    public List<UserEntity> getAllUserDetails() throws UserNotFound;
    public String deleteUser(Long id);
}
