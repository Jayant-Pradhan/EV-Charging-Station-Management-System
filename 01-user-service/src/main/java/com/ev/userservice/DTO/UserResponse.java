package com.ev.userservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private long id;
    private String name;
    private String mail;
    private String phone;
    private String role;
    private String vehicleNumber;
    private LocalDateTime createdAt;
}
