package com.ev.userservice.Exception;

public class UserNotFoundException  extends RuntimeException{

    public UserNotFoundException(String message){
        super(message);
    }
}
