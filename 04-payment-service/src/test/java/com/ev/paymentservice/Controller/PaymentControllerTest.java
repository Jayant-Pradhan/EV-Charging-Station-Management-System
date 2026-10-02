package com.ev.paymentservice.Controller;

import com.ev.paymentservice.DTO.PaymentRequest;
import com.ev.paymentservice.DTO.PaymentResponse;
import com.ev.paymentservice.Service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;


    @Test
    void paymentCreate_success() {

        PaymentRequest request =
                new PaymentRequest(500.0, "UPI", "BOOK-101", 10L);

        when(paymentService.createPayment(request))
                .thenReturn("Payment created successfully : PAY-101");

        ResponseEntity<String> response =
                paymentController.paymentCreate(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(
                "Payment created successfully : PAY-101",
                response.getBody()
        );

        verify(paymentService).createPayment(request);
    }


    @Test
    void getPaymentById_success() {

        PaymentResponse paymentResponse = new PaymentResponse(
                "PAY-101",
                "BOOK-101",
                10L,
                500.0,
                "UPI",
                "SUCCESS",
                "TXN-101",
                LocalDateTime.of(2026, 10, 2, 10, 0),
                LocalDateTime.of(2026, 10, 2, 10, 5)
        );

        when(paymentService.getPaymentById("PAY-101"))
                .thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response =
                paymentController.getPaymentById("PAY-101");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "PAY-101",
                response.getBody().getPaymentId()
        );

        assertEquals(
                "BOOK-101",
                response.getBody().getBookingId()
        );

        verify(paymentService)
                .getPaymentById("PAY-101");
    }


    @Test
    void getPaymentByBookingId_success() {

        PaymentResponse paymentResponse = new PaymentResponse(
                "PAY-101",
                "BOOK-101",
                10L,
                500.0,
                "UPI",
                "SUCCESS",
                "TXN-101",
                LocalDateTime.of(2026, 10, 2, 10, 0),
                LocalDateTime.of(2026, 10, 2, 10, 5)
        );

        when(paymentService.getPaymentByBookingId("BOOK-101"))
                .thenReturn(paymentResponse);

        ResponseEntity<PaymentResponse> response =
                paymentController.getPaymentByBookingId("BOOK-101");

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "PAY-101",
                response.getBody().getPaymentId()
        );

        assertEquals(
                "BOOK-101",
                response.getBody().getBookingId()
        );

        verify(paymentService)
                .getPaymentByBookingId("BOOK-101");
    }
}