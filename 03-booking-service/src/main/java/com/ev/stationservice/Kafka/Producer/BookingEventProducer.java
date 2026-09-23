package com.ev.stationservice.Kafka.Producer;

import com.ev.stationservice.Kafka.ConsumerDataStore;
import com.ev.stationservice.Kafka.Event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {


    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String BOOKING_DETAILS_TOPIC = "booking.details";

    public void publishingBookingDetailsToEvent(BookingCreatedEvent bookingCreatedEvent){
        publishingBookingDetailsWithRetry(bookingCreatedEvent,1);
    }

    private void publishingBookingDetailsWithRetry(BookingCreatedEvent bookingCreatedEvent, int attempt) {

        kafkaTemplate.send(BOOKING_DETAILS_TOPIC ,bookingCreatedEvent.getBookingId() , bookingCreatedEvent).whenComplete(
                (result,exception) -> {
                    if(exception == null){
                        log.info("booking details publishes with attempt : {} " , attempt);
                    }
                    else{
                        if(attempt < 3){
                            log.warn("booking details publishes to event with the attempt : {} " , attempt);
                            publishingBookingDetailsWithRetry(bookingCreatedEvent , attempt +1);
                        }
                        else{
                            log.error("booking details failed to send after the maximum attempt of : {} " , attempt);
                        }
                    }
                }
        );
    }
}
