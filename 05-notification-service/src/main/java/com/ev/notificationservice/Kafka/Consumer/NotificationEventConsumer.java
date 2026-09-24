package com.ev.notificationservice.Kafka.Consumer;

import com.ev.notificationservice.Kafka.Event.BookingCreatedEvent;
import com.ev.notificationservice.Kafka.Event.PaymentCompletedEvent;
import com.ev.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;




    @KafkaListener(topics = "payment.details" , groupId = "notification-group")
    public void consumingEventFromPaymentService(PaymentCompletedEvent paymentCompletedEvent){

        log.info("----------------------------   Payment details consuming ----------------------------- ");
        log.info("payment id : {} ", paymentCompletedEvent.getPaymentId());
        log.info("booking id : {} ", paymentCompletedEvent.getBookingId());
        log.info("user id : {} ", paymentCompletedEvent.getUserId());
        log.info("amount : {} ", paymentCompletedEvent.getAmount());
        log.info("payment status : {} ", paymentCompletedEvent.getPaymentStatus());
        log.info("transaction id : {} ", paymentCompletedEvent.getTransactionId());
        log.info("created at : {} ", paymentCompletedEvent.getCreatedAt());

        notificationService.sendPaymentNotification(paymentCompletedEvent);
    }





















}
