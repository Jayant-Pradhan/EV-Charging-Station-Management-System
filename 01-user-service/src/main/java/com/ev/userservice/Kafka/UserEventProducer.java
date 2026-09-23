package com.ev.userservice.Kafka;

import com.ev.userservice.KafkaDTO.EventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String USER_DETAILS_TOPIC = "user.details";

    public void sendingToKafkaEvent(EventData eventData){
        sendingWithRetry(eventData,1);
    }

    public void sendingWithRetry(EventData eventData,int attempt){


        kafkaTemplate.send(USER_DETAILS_TOPIC,String.valueOf(eventData.getUserId()),eventData)
                .whenComplete((result,exception)->{
                    if(exception == null){  /// exception nhi hai
                        log.info("data sent to Kafka topic successfully : ");
                    }
                    else{ // exception != null ( exception hai )
                        if(attempt < 3){
                            log.info("Retrying Kafka event, attempt: {}", attempt + 1);
                            sendingWithRetry(eventData,attempt+1);

                        }
                        else{
                            log.warn("data failed to send Kafka event after {} attempts", attempt, exception);
                        }
                    }
                });
    }
}
