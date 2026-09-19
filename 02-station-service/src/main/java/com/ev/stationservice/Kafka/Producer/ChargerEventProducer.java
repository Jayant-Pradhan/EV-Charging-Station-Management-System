package com.ev.stationservice.Kafka.Producer;

import com.ev.stationservice.Kafka.Event.ChargerAddedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChargerEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    private static final String CHARGER_DETAILS_TOPIC = "charger.details";

    public void producingChargerEvent(ChargerAddedEvent chargerAddedEvent){
        producingChargerEventWithRetry(chargerAddedEvent,1);
    }


    public void producingChargerEventWithRetry(ChargerAddedEvent chargerAddedEvent,int attempt){
        kafkaTemplate.send(CHARGER_DETAILS_TOPIC , chargerAddedEvent.getChargerId() , chargerAddedEvent).whenComplete(
                (result,exception)->{
                    if(exception ==  null){
                        log.info("produce event with the attempt of : {} " , attempt);
                    }
                    else{
                        if(attempt < 3){
                            producingChargerEventWithRetry(chargerAddedEvent,attempt+1);
                            log.warn("produce event with ta attempt of : {} " , attempt);
                        }
                    }
                }
        );
    }
}
