package com.ev.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Payment_Application {

    public static void main(String[] args) {
        SpringApplication.run(Payment_Application.class, args);
    }

}
