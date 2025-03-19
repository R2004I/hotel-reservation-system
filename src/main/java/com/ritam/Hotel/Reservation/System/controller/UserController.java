package com.ritam.Hotel.Reservation.System.controller;

import com.ritam.Hotel.Reservation.System.dto.AdminDTO;
import com.ritam.Hotel.Reservation.System.dto.CustomerDTO;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.security.JwtConfig;
import com.ritam.Hotel.Reservation.System.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("hotel/")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtConfig jwt;

    @PostMapping("/add/user")
    public ResponseEntity<UserEntity> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        System.out.println("received customer dto"+ customerDTO);

            System.out.println("before service");
            UserEntity user = userService.addUser(customerDTO);
            System.out.println("after service");
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/add/admin")
    public ResponseEntity<UserEntity> addAdmin( @Valid @RequestBody AdminDTO adminDTO) {
        System.out.println("received admindto"+adminDTO);
            UserEntity user = userService.addUserAdmin(adminDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PutMapping("/user/{userId}/password")
    public ResponseEntity<UserEntity> changePassword(@PathVariable Long userId, @RequestParam String newPassword)
    {
            UserEntity user = userService.changePassword(userId, newPassword);
            return ResponseEntity.ok(user);
    }


    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserEntity> getUserDetails(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @PathVariable Long userId)
    {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){
                UserEntity user = userService.getUserDetails(userId);
                return ResponseEntity.ok(user);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/all")
    public ResponseEntity<List<UserEntity>> getAllUsers(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if(jwt.validateToken(token)){

                List<UserEntity> users = userService.getAllUserDetails();
                return ResponseEntity.ok(users);
            } else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            } }
        else {
            return new ResponseEntity<>(HttpStatus.valueOf("Authorization header is missing or malformed!"));
        }
    }
}
