package com.ritam.Hotel.Reservation.System.controller;

import com.ritam.Hotel.Reservation.System.dto.LoginRequest;
import com.ritam.Hotel.Reservation.System.entity.UserEntity;
import com.ritam.Hotel.Reservation.System.exception.UserNotFound;
import com.ritam.Hotel.Reservation.System.repository.UserRepository;
import com.ritam.Hotel.Reservation.System.security.JwtConfig;
import com.ritam.Hotel.Reservation.System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/user")
    public UserEntity findUser(@RequestParam String email){
        return repo.findByEmail(email).orElseThrow(()-> new UserNotFound("not found"));
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtConfig.generateToken(loginRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }


}
