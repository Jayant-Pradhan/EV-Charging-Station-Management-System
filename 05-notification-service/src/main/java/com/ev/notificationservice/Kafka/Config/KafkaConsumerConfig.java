package com.ev.notificationservice.Kafka.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Bean
    public DefaultErrorHandler defaultErrorHandler(){

        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 3);

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        return new DefaultErrorHandler(recoverer,fixedBackOff);


    }
}
