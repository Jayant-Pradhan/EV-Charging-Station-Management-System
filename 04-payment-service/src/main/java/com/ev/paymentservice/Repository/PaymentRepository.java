package com.ev.paymentservice.Repository;

import com.ev.paymentservice.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment , String> {

    Optional<Payment> findByBookingId(String bookingId);
}
