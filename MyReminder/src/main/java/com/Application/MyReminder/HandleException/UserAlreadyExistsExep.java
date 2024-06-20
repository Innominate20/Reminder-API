package com.Application.MyReminder.HandleException;

public class UserAlreadyExistsExep extends RuntimeException{

    public UserAlreadyExistsExep(String message) {
        super(message);
    }

    public UserAlreadyExistsExep(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsExep(Throwable cause) {
        super(cause);
    }
}

