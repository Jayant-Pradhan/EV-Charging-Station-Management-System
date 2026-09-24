package com.ev.notificationservice.Service;

import com.ev.notificationservice.DTO.UserResponse;
import com.ev.notificationservice.Kafka.Event.BookingCreatedEvent;
import com.ev.notificationservice.Kafka.Event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate;

    private final String userServiceUrl = "http://localhost:8080/user/get/";

    public void sendBookingConfirmation(BookingCreatedEvent event){

        System.out.println("1. BOOKING NOTIFICATION STARTED");

        UserResponse userResponse =
                restTemplate.getForObject(userServiceUrl + event.getUserId() , UserResponse.class);

        System.out.println("2. USER RESPONSE = " + userResponse);
        System.out.println("3. USER EMAIL = " + userResponse.getMail());

        SimpleMailMessage message = new SimpleMailMessage();
        System.out.println("USER RESPONSE = " + userResponse);
        System.out.println("USER EMAIL = " + userResponse.getMail());
        message.setFrom("sarojpradhan2668@gmail.com");
        message.setTo(userResponse.getMail());
        message.setSubject("EV Charging Booking Confirmed");
        message.setText("Booking Confirmed\n\n" +
                        "Booking ID: " + event.getBookingId() + "\n" + "Station ID: " + event.getStationId() + "\n" +
                        "Charger ID: " + event.getChargerId() + "\n" + "Booking Date: " + event.getBookingDate() + "\n" +
                        "Start Time: " + event.getStartTime() + "\n" + "End Time: " + event.getEndTime() + "\n" +
                        "Status: " + event.getStatus() + "\n"
        );
        try{
            System.out.println("4. BEFORE MAIL SEND");
            javaMailSender.send(message);
            System.out.println("5. AFTER MAIL SEND");
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    public void sendPaymentNotification(PaymentCompletedEvent event){

        System.out.println("1. PAYMENT NOTIFICATION STARTED");

        UserResponse userResponse = restTemplate.getForObject(userServiceUrl + event.getUserId() , UserResponse.class);

        System.out.println("2. USER RESPONSE = " + userResponse);
        System.out.println("3. USER EMAIL = " + userResponse.getMail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("sarojpradhan2668@gmail.com");
        message.setTo(userResponse.getMail());
        message.setSubject("EV Charging Payment Confirmation");
        message.setText(
                        "Payment Completed\n\n" +
                        "Payment ID: " + event.getPaymentId() + "\n" +
                        "Booking ID: " + event.getBookingId() + "\n" +
                        "User ID: " + event.getUserId() + "\n" +
                        "Amount: " + event.getAmount() + "\n" +
                        "Payment Status: " + event.getPaymentStatus() + "\n" +
                        "Transaction ID: " + event.getTransactionId() + "\n" +
                        "Created At: " + event.getCreatedAt() + "\n"
        );
        try{
            System.out.println("4. BEFORE PAYMENT MAIL SEND");

            javaMailSender.send(message);

            System.out.println("5. AFTER PAYMENT MAIL SEND");
        }
        catch(Exception  e){
            e.printStackTrace();
            throw  e;
        }

    }

}
