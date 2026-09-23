package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.Cache.StationCacheService;
import com.ev.stationservice.Kafka.ConsumerDataStore;
import com.ev.stationservice.Kafka.Event.StationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StationEventConsumer {
    private final ConsumerDataStore consumerDataStore;
    private final StationCacheService stationCacheService;

    @KafkaListener(topics = "station.details" , groupId = "booking-group",
                properties = {"spring.json.value.default.type=com.ev.stationservice.Kafka.Event.StationCreatedEvent"})
    public void consumingEventFromStationService(StationCreatedEvent stationCreatedEvent){
        log.info("----------------- Station Details in : booking service : ---------------");
        log.info("station id : {} " ,stationCreatedEvent.getStationId() );
        log.info("station name : {} " , stationCreatedEvent.getStationName());
        log.info("latitude is  : {} " , stationCreatedEvent.getLatitude());
        log.info("longitude is : {} " , stationCreatedEvent.getLongitude());
        log.info("status is : {} " , stationCreatedEvent.getStatus());

        consumerDataStore.addStationId(stationCreatedEvent.getStationId());

        log.info("StationId stored in ConsumerDataStore: {}",
                stationCreatedEvent.getStationId());

        stationCacheService.cacheStation(stationCreatedEvent);
    }
}
