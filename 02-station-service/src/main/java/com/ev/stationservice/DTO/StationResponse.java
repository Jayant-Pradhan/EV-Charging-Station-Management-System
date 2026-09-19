package com.ev.stationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationResponse {
    private String stationId;
    private String stationName;
    private Double latitude;
    private Double longitude;
    private String status;
}
