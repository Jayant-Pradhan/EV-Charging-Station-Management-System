package com.ev.notificationservice.Kafka.Consumer;

import com.ev.notificationservice.Kafka.Event.BookingCreatedEvent;
import com.ev.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final NotificationService notificationService;


    @KafkaListener(topics = "booking.details" , groupId = "notification-group")
    public void consumingEventFromBookingService(BookingCreatedEvent bookingCreatedEvent){

        log.info("----------------------------   Booking details consuming ----------------------------- ");
        log.info("booking id : {} " , bookingCreatedEvent.getBookingId());
        log.info("user id : {} " , bookingCreatedEvent.getUserId());
        log.info("Station id  : {} " , bookingCreatedEvent.getStationId());
        log.info("charger Id : {} " , bookingCreatedEvent.getChargerId());
        log.info("bookingDate : {} " , bookingCreatedEvent.getBookingDate());
        log.info("start time : {} " , bookingCreatedEvent.getStartTime());
        log.info("end Time : {} " , bookingCreatedEvent.getEndTime());
        log.info("Status : {} " , bookingCreatedEvent.getStatus());
        log.info("Created At : {} " , bookingCreatedEvent.getCreatedAt());
        notificationService.sendBookingConfirmation(bookingCreatedEvent);

    }
}
