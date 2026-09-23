package com.ev.stationservice.Kafka;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ConsumerDataStore {

    private final Set<Long> userIds = new HashSet<>();
    private final Set<String> stationIds = new HashSet<>();
    private final Set<String> chargerIds = new HashSet<>();

    public void addUserId(Long userId){
        userIds.add(userId);

    }

    public void addStationId(String stationId){
        stationIds.add(stationId);
    }

    public void addChargerId(String chargerId){
        chargerIds.add(chargerId);
    }

    // -------------------------  //

    public boolean containsUserId(long userId){
        return userIds.contains(userId);
    }

    public boolean containsStationId(String stationId){
        return stationIds.contains(stationId);
    }

    public boolean containsChargerId(String chargerId){
        return chargerIds.contains(chargerId);
    }


}
