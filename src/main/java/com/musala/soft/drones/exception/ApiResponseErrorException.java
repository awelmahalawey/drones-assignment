package com.musala.soft.drones.exception;

import org.springframework.http.HttpStatus;

public class ApiResponseErrorException extends BaseRunTimeException {

    private HttpStatus statusCode;

    public ApiResponseErrorException(String errorName, String message, HttpStatus statusCode) {
        super(errorName, message);
        this.statusCode = statusCode;
    }
}
