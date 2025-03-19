package com.ritam.Hotel.Reservation.System.service.impl;

import com.ritam.Hotel.Reservation.System.dto.emailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("$(spring.mail.username)")
    private String fromEmailId;

    public void sendMail(emailDTO email)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmailId);
        message.setSubject(email.getSubject());
        message.setTo(email.getRecipient());
        message.setText(email.getBody());

        javaMailSender.send(message);
    }
}
