package com.ev.stationservice.Kafka.Consumer;

import com.ev.stationservice.Kafka.Event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventConsumer {

    @KafkaListener(topics = "user.details" , groupId = "booking-group")
    public void consumingEventFromUserService(UserCreatedEvent userCreatedEvent){

        log.info("---------------  User Details : in booking service : ------------------------- ");
        log.info("user name : {} " , userCreatedEvent.getName());
        log.info("users mobile number : {} " , userCreatedEvent.getPhone());
        log.info("user mail id : {} " , userCreatedEvent.getMail());
        log.info("user's role : {} " , userCreatedEvent.getRole());
        log.info("user vehicleNumber : {} " , userCreatedEvent.getVehicleNumber());
        log.info("created At : {} " , userCreatedEvent.getCreatedAt());

    }
}
