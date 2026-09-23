package com.ev.paymentservice.Kafka.Producer;

import com.ev.paymentservice.Kafka.Event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String,Object> kafkaTemplate;
    private static final String PAYMENT_DETAILS_TOPIC = "payment.details";

    public void publishingEventFromPayment(PaymentCompletedEvent completedEvent){
        publishingEventFromPaymentWithRetry(completedEvent ,1);
    }

    private void publishingEventFromPaymentWithRetry(PaymentCompletedEvent completedEvent, int attempt) {

        kafkaTemplate.send(PAYMENT_DETAILS_TOPIC , completedEvent.getPaymentId() , completedEvent).whenComplete(
                (result , exception)->{
                    if(exception == null){
                        log.info("event published without any attempt : {} " , attempt);
                    }
                    else{
                        if(attempt < 3){
                            log.info("event published with the attempted of : {} " , attempt);
                            publishingEventFromPaymentWithRetry(completedEvent , attempt+1);
                        }
                        else{
                            log.error("event could not published even after maximum attempt");
                        }
                    }
                }
        );
    }

}
