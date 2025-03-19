package com.ritam.Hotel.Reservation.System.controller;

import com.ritam.Hotel.Reservation.System.dto.emailDTO;
import com.ritam.Hotel.Reservation.System.service.impl.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class emailController {

    @Autowired
    private SendMail sendMail;

    @PostMapping("/send/mail")
    public ResponseEntity<String> sendMail(@RequestBody emailDTO email)
    {
        sendMail.sendMail(email);
        return ResponseEntity.status(HttpStatus.OK).body("Mail send Successfully");
    }
}
