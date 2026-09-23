package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.Cache.UserCacheService;
import com.ev.stationservice.Kafka.ConsumerDataStore;
import com.ev.stationservice.Kafka.Event.UserCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEventConsumer {
    private final ConsumerDataStore consumerDataStore;
    private final UserCacheService userCacheService;

    @KafkaListener(topics = "user.details" , groupId = "booking-group",
                   properties = {"spring.json.value.default.type=com.ev.stationservice.Kafka.Event.UserCreatedEvent"})
    public void consumingEventFromUserService(UserCreatedEvent userCreatedEvent){

        log.info("---------------  User Details : in booking service : ------------------------- ");
        log.info("user name : {} " , userCreatedEvent.getName());
        log.info("users mobile number : {} " , userCreatedEvent.getPhone());
        log.info("user mail id : {} " , userCreatedEvent.getMail());
        log.info("user's role : {} " , userCreatedEvent.getRole());
        log.info("user vehicleNumber : {} " , userCreatedEvent.getVehicleNumber());
        log.info("created At : {} " , userCreatedEvent.getCreatedAt());

        consumerDataStore.addUserId(userCreatedEvent.getUserId());

        log.info("Consumed userId stored in ConsumerDataStore: {}", userCreatedEvent.getUserId());

    }
}
