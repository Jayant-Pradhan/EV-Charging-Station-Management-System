package com.ev.paymentservice.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    private String paymentId;
    private String bookingId;
    private long userId;

    private Double amount;
    private String paymentMethod;

    private String paymentStatus;
    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
