package com.musala.soft.drones.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BaseRunTimeException extends RuntimeException {

    private String errorName;
    private String message;
    private LocalDateTime timestamp;

    public BaseRunTimeException(Exception exception) {
        super(exception);
        this.timestamp = LocalDateTime.now();
    }

    public BaseRunTimeException(String errorName) {
        this.errorName = errorName;
        this.timestamp = LocalDateTime.now();
    }

    public BaseRunTimeException(String errorName, String errorMessage) {
        this.errorName = errorName;
        this.message = errorMessage;
        this.timestamp = LocalDateTime.now();
    }
}

