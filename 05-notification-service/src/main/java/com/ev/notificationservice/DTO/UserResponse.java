package com.ev.notificationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long id;
    private String name;
    private String mail;
    private String phone;
    private String role;
    private String vehicleNumber;
    private LocalDateTime createdAt;
}
