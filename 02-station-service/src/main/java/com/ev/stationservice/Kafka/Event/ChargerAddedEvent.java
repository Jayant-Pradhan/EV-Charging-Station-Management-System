package com.ev.stationservice.Kafka.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargerAddedEvent {
    private String chargerId;
    private String chargerNumber;
    private String chargerType;
    private Double power;
    private String status;
}
