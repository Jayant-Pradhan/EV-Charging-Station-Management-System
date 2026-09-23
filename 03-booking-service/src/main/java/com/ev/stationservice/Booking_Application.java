package com.ev.stationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Booking_Application {

    public static void main(String[] args) {
        SpringApplication.run(Booking_Application.class, args);
    }

}
