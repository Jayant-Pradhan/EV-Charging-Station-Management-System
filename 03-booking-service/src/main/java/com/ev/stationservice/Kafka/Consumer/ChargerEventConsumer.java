package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.ConsumerDataStore;
import com.ev.stationservice.Kafka.Event.ChargerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChargerEventConsumer {

    private final ConsumerDataStore consumerDataStore;

    @KafkaListener(topics = "charger.details" , groupId = "booking-group", properties = {"spring.json.value.default.type=com.ev.stationservice.Kafka.Event.ChargerCreatedEvent"})
    public void consumingFromChargerServiceDetails(ChargerCreatedEvent chargerCreatedEvent){
        log.info("----------------- Charger Details in : booking service : -----------------");
        log.info("charger id : {}", chargerCreatedEvent.getChargerId());
        log.info("charger number : {}", chargerCreatedEvent.getChargerNumber());
        log.info("charger type : {}", chargerCreatedEvent.getChargerType());
        log.info("charger power : {}", chargerCreatedEvent.getPower());
        log.info("charger status : {}", chargerCreatedEvent.getStatus());

        consumerDataStore.addChargerId(chargerCreatedEvent.getChargerId());

        log.info("ChargerId stored in ConsumerDataStore: {}",
                chargerCreatedEvent.getChargerId());
    }
}
