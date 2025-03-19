package com.ritam.Hotel.Reservation.System.controller;

import com.razorpay.RazorpayException;
import com.ritam.Hotel.Reservation.System.dto.PaymentDTO;
import com.ritam.Hotel.Reservation.System.service.BookingService;
import com.ritam.Hotel.Reservation.System.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("payment/")
public class PaymentController {

    private final PaymentService paymentService;

    private final BookingService service;

    public PaymentController(PaymentService paymentService, BookingService service) {
        this.paymentService = paymentService;
        this.service = service;
    }

    @PostMapping("/initiate/{bookingId}")
    public ResponseEntity<Map<String, Object>> initiatePayment(@PathVariable Long bookingId) {
        try {
            String orderId = paymentService.initiatePayment(bookingId);
            double amount = service.calculateTotalPrice(bookingId); // Fetch the total price of booking
            System.out.println(amount);
            Map<String, Object> response = new HashMap<>();
            response.put("order_id", orderId); // ✅ Use "order_id" instead of "orderId"
            response.put("amount", (int) (amount * 100)); // ✅ Ensure integer paise value
            response.put("currency", "INR");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RazorpayException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }


    @PostMapping("/verify")
    public ResponseEntity<?> finalizePayment(@RequestBody PaymentDTO dto){
        try {
            String confirmCode = paymentService.capturePayment(dto.getRazorpayPaymentId(), dto.getRazorpayOrderId(), dto.getRazorpaySignature());
            Map<String, Object> response = new HashMap<>();
            response.put("confirmCode", confirmCode);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e){
            e.getMessage();
        }
       return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Failed To generate booking id");
    }

//
}
