package com.ev.paymentservice.Kafka.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private String paymentId;
    private String bookingId;
    private long userId;
    private Double amount;
    private String paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
}
