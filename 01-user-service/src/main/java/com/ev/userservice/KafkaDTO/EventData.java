package com.ev.userservice.KafkaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private long id;
    private String name;
    private String mail;
    private String phone;
    private String role;
    private String vehicleNumber;
    private LocalDateTime createdAt;
}
