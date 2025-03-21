package com.ritam.Hotel.Reservation.System.repository;

import com.ritam.Hotel.Reservation.System.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByRazorpayOrderId(String orderId);
}
