package com.Application.MyReminder.HandleException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler
    ResponseEntity<UserErrorResponse> handleException(UserAlreadyExistsExep exe){
        UserErrorResponse error = new UserErrorResponse();
        error.setMessage(exe.getMessage());
        error.setStatus(HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

}
