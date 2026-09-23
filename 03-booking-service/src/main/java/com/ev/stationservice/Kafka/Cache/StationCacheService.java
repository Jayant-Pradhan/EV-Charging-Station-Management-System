package com.ev.stationservice.Kafka.Cache;

import com.ev.stationservice.Kafka.Event.StationCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StationCacheService {
    @CachePut(value = "stations", key = "#stationCreatedEvent.getStationId()")
    public StationCreatedEvent cacheStation(StationCreatedEvent stationCreatedEvent) {

        log.info("Putting station {} into Redis cache", stationCreatedEvent.getStationId());
        return stationCreatedEvent;
    }
}
