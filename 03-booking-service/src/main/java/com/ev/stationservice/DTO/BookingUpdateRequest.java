package com.ev.stationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequest {
    private String stationId;
    private String chargerId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
