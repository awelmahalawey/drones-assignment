package com.musala.soft.drones.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponseErrorException extends BaseRunTimeException {

    private HttpStatus statusCode;

    public ApiResponseErrorException(String errorName, String message, HttpStatus statusCode) {
        super(errorName, message);
        this.statusCode = statusCode;
    }
}
