package com.ev.stationservice.Kafka.Cache;

import com.ev.stationservice.Kafka.Event.UserCreatedEvent;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService {

    @CachePut(value = "users", key = "#userCreatedEvent.getUserId()")
    public UserCreatedEvent cacheUser(UserCreatedEvent userCreatedEvent){
        return userCreatedEvent;
    }
}
