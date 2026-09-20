package com.ev.stationservice.Exception;

public class BookingNotFoundException  extends RuntimeException{



    public BookingNotFoundException (String message){
        super(message);
    }
}
