package com.musala.soft.drones.exception;

public class DataValidationException extends BaseRunTimeException{

    public DataValidationException(Exception exception) {
        super(exception);
    }

    public DataValidationException(String errorName) {
        super(errorName);
    }
}
