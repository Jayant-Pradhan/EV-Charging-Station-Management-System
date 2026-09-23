package com.ev.paymentservice.Controller;

import com.ev.paymentservice.DTO.PaymentRequest;
import com.ev.paymentservice.DTO.PaymentResponse;
import com.ev.paymentservice.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // Create Payment
    @PostMapping("/create")
    public ResponseEntity<String> paymentCreate(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(paymentRequest));
    }

    // Get Payment by ID
    @GetMapping("/getBy/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable String paymentId){
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    // Get Payment by Booking ID
    @GetMapping("/getByBookingId/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable String bookingId){
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

}
