package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.Event.ChargerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChargerEventConsumer {

    @KafkaListener(topics = "charger.details" , groupId = "booking-group")
    public void consumingFromChargerServiceDetails(ChargerCreatedEvent chargerCreatedEvent){
        log.info("----------------- Charger Details in : booking service : -----------------");
        log.info("charger id : {}", chargerCreatedEvent.getChargerId());
        log.info("charger number : {}", chargerCreatedEvent.getChargerNumber());
        log.info("charger type : {}", chargerCreatedEvent.getChargerType());
        log.info("charger power : {}", chargerCreatedEvent.getPower());
        log.info("charger status : {}", chargerCreatedEvent.getStatus());
    }
}
