package com.ev.paymentservice.Kafka.Consumer;

import com.ev.paymentservice.Kafka.Event.BookingCreatedEvent;
import com.ev.paymentservice.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "booking.details" , groupId = "payment-group")
    public void consumingEventFromBookingService(BookingCreatedEvent bookingCreatedEvent){

        log.info("--------------  booking details : -------------------- ");
        log.info("Booking id : {} " , bookingCreatedEvent.getBookingId());
        log.info("User Id : {} " , bookingCreatedEvent.getUserId());
        log.info("Station id : {} " , bookingCreatedEvent.getStationId());
        log.info("Charger id : {} " , bookingCreatedEvent.getChargerId());
        log.info("Booking  date : {}" , bookingCreatedEvent.getBookingDate());
        log.info("Start time : {}" , bookingCreatedEvent.getStartTime());
        log.info("end time : {} " , bookingCreatedEvent.getEndTime());
        log.info("Created At : {} " , bookingCreatedEvent.getCreatedAt());

        paymentService.updateBookingDetails(bookingCreatedEvent);
    }
}
