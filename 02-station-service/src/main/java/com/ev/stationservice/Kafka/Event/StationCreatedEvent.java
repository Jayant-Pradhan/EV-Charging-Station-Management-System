package com.ev.stationservice.Kafka.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationCreatedEvent {
    private String stationId;
    private String stationName;
    private Double latitude;
    private Double longitude;
    private String status;
}
