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
    private String errorCodePrefix = "GCE-";
    private String errorCode;
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

    public BaseRunTimeException(String errorName, String errorCodePrefix) {
        this.errorName = errorName;
        this.errorCodePrefix = errorCodePrefix;
        this.timestamp = LocalDateTime.now();
    }

    public BaseRunTimeException(String errorName, String message, String errorCodePrefix, String errorCode) {
        this.errorName = errorName;
        this.message = message;
        this.errorCodePrefix = errorCodePrefix;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() {
        return errorCodePrefix + errorCode;
    }
}

