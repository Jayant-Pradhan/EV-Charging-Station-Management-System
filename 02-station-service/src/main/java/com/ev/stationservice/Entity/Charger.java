package com.ev.stationservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "charger")
public class Charger {

    @Id
    private String chargerId;
    private String chargerNumber;
    private String chargerType;
    private Double power;
    private String status;
    @ManyToOne
    private Station station;
}
