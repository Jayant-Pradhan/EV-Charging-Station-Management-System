package com.ev.paymentservice.Service;

import com.ev.paymentservice.DTO.PaymentRequest;
import com.ev.paymentservice.DTO.PaymentResponse;
import com.ev.paymentservice.Entity.Payment;
import com.ev.paymentservice.Exception.PaymentNotFoundException;
import com.ev.paymentservice.Kafka.Event.BookingCreatedEvent;
import com.ev.paymentservice.Kafka.Producer.PaymentEventProducer;
import com.ev.paymentservice.Repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentService paymentService;


    // ---------------------------------------------------------
    // createPayment()
    // ---------------------------------------------------------

    @Test
    void createPayment_success() {

        PaymentRequest request =
                new PaymentRequest(500.0, "UPI", "BOOK-101", 10L);

        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("BOOK-101");
        event.setUserId(10L);

        paymentService.updateBookingDetails(event);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = paymentService.createPayment(request);

        assertTrue(result.startsWith("Payment created successfully : "));

        verify(paymentRepository, times(1))
                .save(any(Payment.class));

        verify(paymentEventProducer, times(1))
                .publishingEventFromPayment(any());
    }


    @Test
    void createPayment_bookingOrUserValidationFailed() {

        PaymentRequest request =
                new PaymentRequest(500.0, "UPI", "BOOK-101", 10L);

        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("BOOK-999");
        event.setUserId(99L);

        paymentService.updateBookingDetails(event);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.createPayment(request)
        );

        assertEquals(
                "Booking id or user id validate failed ",
                exception.getMessage()
        );

        verify(paymentRepository, never())
                .save(any(Payment.class));

        verify(paymentEventProducer, never())
                .publishingEventFromPayment(any());
    }


    // ---------------------------------------------------------
    // updateBookingDetails()
    // ---------------------------------------------------------

    @Test
    void updateBookingDetails_success() {

        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId("BOOK-200");
        event.setUserId(20L);

        paymentService.updateBookingDetails(event);

        PaymentRequest request =
                new PaymentRequest(1000.0, "CARD", "BOOK-200", 20L);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = paymentService.createPayment(request);

        assertTrue(result.startsWith("Payment created successfully : "));

        verify(paymentRepository).save(any(Payment.class));
        verify(paymentEventProducer)
                .publishingEventFromPayment(any());
    }


    // ---------------------------------------------------------
    // getPaymentById()
    // ---------------------------------------------------------

    @Test
    void getPaymentById_success() {

        Payment payment = createPayment();

        when(paymentRepository.findById("PAY-101"))
                .thenReturn(Optional.of(payment));

        PaymentResponse response =
                paymentService.getPaymentById("PAY-101");

        assertNotNull(response);

        assertEquals("PAY-101", response.getPaymentId());
        assertEquals("BOOK-101", response.getBookingId());
        assertEquals(10L, response.getUserId());
        assertEquals(500.0, response.getAmount());
        assertEquals("UPI", response.getPaymentMethod());
        assertEquals("SUCCESS", response.getPaymentStatus());
        assertEquals("TXN-101", response.getTransactionId());

        verify(paymentRepository).findById("PAY-101");
    }


    @Test
    void getPaymentById_notFound() {

        when(paymentRepository.findById("PAY-999"))
                .thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentById("PAY-999")
        );

        assertEquals(
                "payment id is not found : PAY-999",
                exception.getMessage()
        );

        verify(paymentRepository).findById("PAY-999");
    }


    // ---------------------------------------------------------
    // getPaymentByBookingId()
    // ---------------------------------------------------------

    @Test
    void getPaymentByBookingId_success() {

        Payment payment = createPayment();

        when(paymentRepository.findByBookingId("BOOK-101"))
                .thenReturn(Optional.of(payment));

        PaymentResponse response =
                paymentService.getPaymentByBookingId("BOOK-101");

        assertNotNull(response);

        assertEquals("PAY-101", response.getPaymentId());
        assertEquals("BOOK-101", response.getBookingId());
        assertEquals(10L, response.getUserId());
        assertEquals(500.0, response.getAmount());
        assertEquals("UPI", response.getPaymentMethod());
        assertEquals("SUCCESS", response.getPaymentStatus());
        assertEquals("TXN-101", response.getTransactionId());

        verify(paymentRepository)
                .findByBookingId("BOOK-101");
    }


    @Test
    void getPaymentByBookingId_notFound() {

        when(paymentRepository.findByBookingId("BOOK-999"))
                .thenReturn(Optional.empty());

        PaymentNotFoundException exception = assertThrows(
                PaymentNotFoundException.class,
                () -> paymentService.getPaymentByBookingId("BOOK-999")
        );

        assertEquals(
                "booking id is not found : BOOK-999",
                exception.getMessage()
        );

        verify(paymentRepository)
                .findByBookingId("BOOK-999");
    }


    // ---------------------------------------------------------
    // Helper
    // ---------------------------------------------------------

    private Payment createPayment() {

        Payment payment = new Payment();

        payment.setPaymentId("PAY-101");
        payment.setBookingId("BOOK-101");
        payment.setUserId(10L);
        payment.setAmount(500.0);
        payment.setPaymentMethod("UPI");
        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionId("TXN-101");

        payment.setCreatedAt(
                LocalDateTime.of(2026, 10, 2, 10, 0)
        );

        payment.setUpdatedAt(
                LocalDateTime.of(2026, 10, 2, 10, 5)
        );

        return payment;
    }
}