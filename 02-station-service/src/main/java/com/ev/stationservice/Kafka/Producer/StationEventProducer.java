package com.ev.stationservice.Kafka.Producer;

import com.ev.stationservice.Kafka.Event.StationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StationEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String STATION_DETAILS_TOPIC = "station.details";

    public void producingStationEvent(StationCreatedEvent stationCreatedEvent){

        producingStationEventRetry(stationCreatedEvent,1);
    }

    public void producingStationEventRetry(StationCreatedEvent stationCreatedEvent,int attempt){

        kafkaTemplate.send(STATION_DETAILS_TOPIC,stationCreatedEvent.getStationId(),stationCreatedEvent).whenComplete(
                (result,exception)-> {
                    if(exception == null){
                        log.info("event produce data with retry of attempt : {} ", attempt);
                    }
                    else{
                        if(attempt < 3){
                            producingStationEventRetry(stationCreatedEvent, attempt+1);
                            log.warn("event produce data with retry of attempt : {} " , attempt);
                        }


                    }
                }
        );
    }
}
