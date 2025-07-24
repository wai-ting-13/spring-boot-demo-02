package me.specter.spring_boot_demo_02.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS)
public class TooManyRequestException extends RuntimeException {
    public TooManyRequestException(String message){
        super(message);
    }
}