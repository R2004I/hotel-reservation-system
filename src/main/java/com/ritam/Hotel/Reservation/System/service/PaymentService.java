package com.ritam.Hotel.Reservation.System.service;

import com.razorpay.RazorpayException;
import com.ritam.Hotel.Reservation.System.entity.Payment;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {

    public String initiatePayment(Long bookingId) throws RazorpayException;
    public String capturePayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) throws RazorpayException;
//    public Payment getPaymentByBooking(Long bookingId);

}
