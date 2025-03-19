package com.ritam.Hotel.Reservation.System.service.impl;

import com.ritam.Hotel.Reservation.System.dto.AdminDTO;
import com.ritam.Hotel.Reservation.System.dto.CustomerDTO;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.exception.UserNotFound;
import com.ritam.Hotel.Reservation.System.repository.UserRepository;
import com.ritam.Hotel.Reservation.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity getUserByEmailId(String emailId) throws UserNotFound {
        UserEntity byEmail = userRepo.findByEmail(emailId).orElseThrow(()-> new UserNotFound("User with email:"+emailId+" not found"));
        return byEmail;
    }

    @Override
    public UserEntity addUser(CustomerDTO customer) throws UserNotFound {

        if (customer == null)
            throw new UserNotFound("customer Can not be Null");
        Optional<UserEntity> findByEmail = userRepo.findByEmail(customer.getEmail());
        if (findByEmail.isPresent()) {
            System.out.println("inside add user method");
            throw new RuntimeException("Email already Register");
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(customer.getName());
        newUser.setEmail(customer.getEmail());
        newUser.setPhoneNumber(customer.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(customer.getPassword()));
        newUser.setAddress(customer.getAddress());
        newUser.setRole("USER");
        return userRepo.save(newUser);
    }

    @Override
    public UserEntity addUserAdmin(AdminDTO admin) throws UserNotFound {
        if (admin == null)
            throw new UserNotFound("admin Can not be Null");
        Optional<UserEntity> findByEmail = userRepo.findByEmail(admin.getEmail());
        if (findByEmail.isPresent()) {
            System.out.println("inside add user method");
            throw new RuntimeException("Email already Register");
        }

        UserEntity newUser = new UserEntity();

        newUser.setName(admin.getName());
        newUser.setEmail(admin.getEmail());
        newUser.setPhoneNumber(admin.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(admin.getPassword()));
        newUser.setAddress(admin.getAddress());
        newUser.setRole("ADMIN");
        newUser.setBookingList(null);
        return userRepo.save(newUser);
    }

    @Override
    public UserEntity changePassword(Long userId, String newPassword) throws UserNotFound{
       UserEntity user = userRepo.findById(userId).orElseThrow(()-> new UserNotFound("User with id"+userId+" not found"));
       if(newPassword.length()>=5 && newPassword.length()<=10)
       {
           user.setPassword(passwordEncoder.encode(newPassword));
           return userRepo.save(user);
       }else {
           throw new RuntimeException("Provide valid password");
       }

    }

    @Override
    public UserEntity getUserDetails(Long userId) throws UserNotFound {
        return userRepo.findById(userId).orElseThrow(()-> new UserNotFound("User with id"+userId+" not found"));
    }

    @Override
    public List<UserEntity> getAllUserDetails() throws UserNotFound {
        List<UserEntity> existingAllUser = userRepo.findAll();
        if (existingAllUser.isEmpty()) {
            throw new RuntimeException("User list is Empty");
        }
        return existingAllUser;
    }
}
