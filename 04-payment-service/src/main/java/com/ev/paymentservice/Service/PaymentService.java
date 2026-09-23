package com.ev.paymentservice.Service;

import com.ev.paymentservice.DTO.PaymentRequest;
import com.ev.paymentservice.DTO.PaymentResponse;
import com.ev.paymentservice.Entity.Payment;
import com.ev.paymentservice.Exception.PaymentNotFoundException;
import com.ev.paymentservice.Kafka.Event.BookingCreatedEvent;
import com.ev.paymentservice.Repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private String consumerBookingId;
    private Long consumerUserId;


    public String createPayment(PaymentRequest paymentRequest) {
        Payment payment = new Payment();

        payment.setPaymentId(UUID.randomUUID().toString());

        payment.setBookingId(paymentRequest.getBookingId());
        payment.setUserId(paymentRequest.getUserId());

        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());

        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionId(UUID.randomUUID().toString());

        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        // consumer validation :
        if(!paymentRequest.getBookingId().equals(consumerBookingId) || paymentRequest.getUserId() != consumerUserId){
            throw new RuntimeException("Booking id or user id validate failed ");
        }
        paymentRepository.save(payment);

        return "Payment created successfully : " + payment.getPaymentId();


    }

    public void updateBookingDetails(BookingCreatedEvent bookingCreatedEvent) {

        consumerBookingId = bookingCreatedEvent.getBookingId();
        consumerUserId = bookingCreatedEvent.getUserId();

        log.info("booking id : {} " , bookingCreatedEvent.getBookingId());
        log.info("user id : {} " , bookingCreatedEvent.getUserId());

    }

    @Cacheable(value = "paymentById", key = "#paymentId")
    public PaymentResponse getPaymentById(String paymentId) {

        Optional<Payment> isAvail = paymentRepository.findById(paymentId);

        if(isAvail.isPresent()){
            Payment payment = isAvail.get();
            PaymentResponse response  = new PaymentResponse();
            response.setPaymentId(payment.getPaymentId());
            response.setBookingId(payment.getBookingId());
            response.setUserId(payment.getUserId());
            response.setAmount(payment.getAmount());
            response.setPaymentMethod(payment.getPaymentMethod());
            response.setPaymentStatus(payment.getPaymentStatus());
            response.setTransactionId(payment.getTransactionId());
            response.setCreatedAt(payment.getCreatedAt());
            response.setUpdatedAt(payment.getUpdatedAt());
            return response;
        }
        else{
            throw new PaymentNotFoundException("payment id is not found : " + paymentId);
        }
    }

    @Cacheable(value = "paymentByBookingId", key = "#bookingId")
    public PaymentResponse getPaymentByBookingId(String bookingId) {
        Optional<Payment> isAvail = paymentRepository.findByBookingId(bookingId);
        if(isAvail.isPresent()){
            Payment payment = isAvail.get();
            PaymentResponse response  = new PaymentResponse();
            response.setPaymentId(payment.getPaymentId());
            response.setBookingId(payment.getBookingId());
            response.setUserId(payment.getUserId());
            response.setAmount(payment.getAmount());
            response.setPaymentMethod(payment.getPaymentMethod());
            response.setPaymentStatus(payment.getPaymentStatus());
            response.setTransactionId(payment.getTransactionId());
            response.setCreatedAt(payment.getCreatedAt());
            response.setUpdatedAt(payment.getUpdatedAt());
            return response;
        }
        else{
            throw new PaymentNotFoundException("booking id is not found : " + bookingId);
        }
    }
}
