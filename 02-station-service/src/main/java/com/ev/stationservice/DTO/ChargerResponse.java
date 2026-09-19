package com.ev.stationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargerResponse {
    private String chargerId;
    private String chargerNumber;
    private String chargerType;
    private Double power;
    private String status;
}
