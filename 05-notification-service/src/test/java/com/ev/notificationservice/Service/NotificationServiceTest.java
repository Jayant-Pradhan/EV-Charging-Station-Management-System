package com.ev.notificationservice.Service;

import com.ev.notificationservice.DTO.UserResponse;
import com.ev.notificationservice.Kafka.Event.BookingCreatedEvent;
import com.ev.notificationservice.Kafka.Event.PaymentCompletedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;


    @Test
    void sendBookingConfirmation_success() {

        BookingCreatedEvent event = new BookingCreatedEvent(
                "BOOK-101",
                10L,
                "ST-101",
                "CH-01",
                LocalDate.of(2026, 10, 2),
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                "CONFIRMED",
                LocalDateTime.of(2026, 10, 2, 9, 30)
        );

        UserResponse userResponse = new UserResponse(
                10L,
                "Jayant",
                "jayant@example.com",
                "9876543210",
                "USER",
                "OD02AB1234",
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        when(restTemplate.getForObject(
                "http://localhost:8080/user/get/10",
                UserResponse.class
        )).thenReturn(userResponse);

        notificationService.sendBookingConfirmation(event);

        verify(restTemplate).getForObject(
                "http://localhost:8080/user/get/10",
                UserResponse.class
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                "sarojpradhan2668@gmail.com",
                message.getFrom()
        );

        assertArrayEquals(
                new String[]{"jayant@example.com"},
                message.getTo()
        );

        assertEquals(
                "EV Charging Booking Confirmed",
                message.getSubject()
        );

        assertTrue(message.getText().contains("BOOK-101"));
        assertTrue(message.getText().contains("ST-101"));
        assertTrue(message.getText().contains("CH-01"));
        assertTrue(message.getText().contains("CONFIRMED"));
    }


    @Test
    void sendBookingConfirmation_mailSendingFails() {

        BookingCreatedEvent event = new BookingCreatedEvent(
                "BOOK-102",
                20L,
                "ST-102",
                "CH-02",
                LocalDate.of(2026, 10, 3),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                "CONFIRMED",
                LocalDateTime.now()
        );

        UserResponse userResponse = new UserResponse(
                20L,
                "User",
                "user@example.com",
                "9999999999",
                "USER",
                "OD01XX0000",
                LocalDateTime.now()
        );

        when(restTemplate.getForObject(
                "http://localhost:8080/user/get/20",
                UserResponse.class
        )).thenReturn(userResponse);

        doThrow(new RuntimeException("Mail server failed"))
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.sendBookingConfirmation(event)
        );

        assertEquals("Mail server failed", exception.getMessage());

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }


    @Test
    void sendPaymentNotification_success() {

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                "PAY-101",
                "BOOK-101",
                10L,
                500.0,
                "SUCCESS",
                "TXN-101",
                LocalDateTime.of(2026, 10, 2, 10, 30)
        );

        UserResponse userResponse = new UserResponse(
                10L,
                "Jayant",
                "jayant@example.com",
                "9876543210",
                "USER",
                "OD02AB1234",
                LocalDateTime.now()
        );

        when(restTemplate.getForObject(
                "http://localhost:8080/user/get/10",
                UserResponse.class
        )).thenReturn(userResponse);

        notificationService.sendPaymentNotification(event);

        verify(restTemplate).getForObject(
                "http://localhost:8080/user/get/10",
                UserResponse.class
        );

        ArgumentCaptor<SimpleMailMessage> captor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(javaMailSender).send(captor.capture());

        SimpleMailMessage message = captor.getValue();

        assertEquals(
                "sarojpradhan2668@gmail.com",
                message.getFrom()
        );

        assertArrayEquals(
                new String[]{"jayant@example.com"},
                message.getTo()
        );

        assertEquals(
                "EV Charging Payment Confirmation",
                message.getSubject()
        );

        assertTrue(message.getText().contains("PAY-101"));
        assertTrue(message.getText().contains("BOOK-101"));
        assertTrue(message.getText().contains("500.0"));
        assertTrue(message.getText().contains("SUCCESS"));
        assertTrue(message.getText().contains("TXN-101"));
    }


    @Test
    void sendPaymentNotification_mailSendingFails() {

        PaymentCompletedEvent event = new PaymentCompletedEvent(
                "PAY-102",
                "BOOK-102",
                20L,
                1000.0,
                "SUCCESS",
                "TXN-102",
                LocalDateTime.now()
        );

        UserResponse userResponse = new UserResponse(
                20L,
                "User",
                "user@example.com",
                "9999999999",
                "USER",
                "OD01XX0000",
                LocalDateTime.now()
        );

        when(restTemplate.getForObject(
                "http://localhost:8080/user/get/20",
                UserResponse.class
        )).thenReturn(userResponse);

        doThrow(new RuntimeException("Payment mail failed"))
                .when(javaMailSender)
                .send(any(SimpleMailMessage.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> notificationService.sendPaymentNotification(event)
        );

        assertEquals("Payment mail failed", exception.getMessage());

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}