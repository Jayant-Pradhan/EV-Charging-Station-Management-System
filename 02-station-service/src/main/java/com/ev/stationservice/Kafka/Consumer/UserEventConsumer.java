package com.ev.stationservice.Kafka.Consumer;


import com.ev.stationservice.Kafka.Event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEventConsumer {


    @KafkaListener(topics = "user.details" , groupId = "station-group")
    public void consumingFromUser(UserRegisteredEvent userRegisteredEvent){
        log.info("----------    User Details : ----------------------");
        log.info("user id of : {} " , userRegisteredEvent.getId());
        log.info("User name : {} " ,userRegisteredEvent.getName());
        log.info("User mail :  {} " , userRegisteredEvent.getMail());
        log.info("User Phone number : {} " ,userRegisteredEvent.getPhone());

    }
}
