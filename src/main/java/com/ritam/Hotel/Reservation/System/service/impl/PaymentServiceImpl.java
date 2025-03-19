package com.ritam.Hotel.Reservation.System.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.ritam.Hotel.Reservation.System.entity.Booking;
import com.ritam.Hotel.Reservation.System.entity.Payment;
import com.ritam.Hotel.Reservation.System.enums.Status;
import com.ritam.Hotel.Reservation.System.exception.BookingNotFound;
import com.ritam.Hotel.Reservation.System.exception.PaymentNotFound;
import com.ritam.Hotel.Reservation.System.helper.RazorpayUtil;
import com.ritam.Hotel.Reservation.System.repository.BookingRepository;
import com.ritam.Hotel.Reservation.System.repository.PaymentRepository;
import com.ritam.Hotel.Reservation.System.service.BookingService;
import com.ritam.Hotel.Reservation.System.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;


    private BookingRepository bookingRepo;
    private PaymentRepository paymentRepo;

    private BookingService service;

    public PaymentServiceImpl(BookingRepository bookingRepo, PaymentRepository paymentRepo, BookingService service) {
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.service = service;
    }

    @Override
    public String initiatePayment(Long bookingId) throws RazorpayException {
        Booking existingBooking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new BookingNotFound("Booking with id " + bookingId + " not found"));

        RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);
        JSONObject options = new JSONObject();
        options.put("amount", service.calculateTotalPrice(bookingId) * 100); // Convert to paise
        options.put("currency", "INR");
        options.put("receipt", "booking_" + bookingId);
        options.put("payment_capture", 1); // Auto-capture payment

        Order order = razorpay.orders.create(options);

        Payment payment = new Payment();
        payment.setBooking(existingBooking);
        payment.setAmount(service.calculateTotalPrice(bookingId));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setRazorpayOrderId(order.get("id"));
        payment.setStatus(Status.IN_PROGRESS);
        paymentRepo.save(payment);
        bookingRepo.save(existingBooking);

        return order.get("id"); // ✅ Return only order ID instead of full object
    }



    public boolean verifyPayment(String orderId, String paymentId, String razorpaySignature) {
        String payload = orderId + '|' + paymentId;
        try {
            return RazorpayUtil.verifySignature(payload, razorpaySignature, razorpaySecret);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String capturePayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
        Payment payment = paymentRepo.findByRazorpayOrderId(razorpayOrderId).orElseThrow(()-> new PaymentNotFound("Payment with id"+ razorpayPaymentId+ " not found"));
        if (payment == null) {
            throw new RuntimeException("Payment record not found for Order ID: " + razorpayOrderId);
        }

       if(verifyPayment(razorpayOrderId,razorpayPaymentId,razorpaySignature)){
           // Update payment details
           payment.setRazorpayPaymentId(razorpayPaymentId);
           payment.setStatus(Status.SUCCESS);
           payment.getBooking().setBookingStatus(Booking.Status.CONFIRMED);
           payment.setPaymentDate(LocalDateTime.now());

           // Generate a booking confirmation code
           payment.getBooking().setBookingConfirmationCode(generateBookingCode());

           // Save updates
           paymentRepo.save(payment);
           bookingRepo.save(payment.getBooking());
       }
       return generateBookingCode();
    }

    public static String generateBookingCode() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return uuid.toUpperCase(); // Optional: Convert to uppercase
    }

//    @Override
//    public Payment getPaymentByBooking(Long bookingId) {
//        return paymentRepo.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Payment not found for booking ID: " + bookingId));
//    }
    }

