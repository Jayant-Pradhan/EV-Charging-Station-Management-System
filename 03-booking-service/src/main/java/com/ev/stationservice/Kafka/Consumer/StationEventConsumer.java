package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.Event.StationCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StationEventConsumer {

    @KafkaListener(topics = "station.details" , groupId = "booking-group")
    public void consumingEventFromStationService(StationCreatedEvent stationCreatedEvent){
        log.info("----------------- Station Details in : booking service : ---------------");
        log.info("station id : {} " ,stationCreatedEvent.getStationId() );
        log.info("station name : {} " , stationCreatedEvent.getStationName());
        log.info("latitude is  : {} " , stationCreatedEvent.getLatitude());
        log.info("longitude is : {} " , stationCreatedEvent.getLongitude());
        log.info("status is : {} " , stationCreatedEvent.getStatus());
    }
}
