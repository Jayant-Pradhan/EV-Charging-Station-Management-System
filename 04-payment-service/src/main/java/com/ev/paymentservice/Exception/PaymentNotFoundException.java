package com.ev.paymentservice.Exception;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String message){
        super(message);
    }
}
