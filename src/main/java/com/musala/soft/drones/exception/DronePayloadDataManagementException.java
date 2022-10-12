package com.musala.soft.drones.exception;

public class DronePayloadDataManagementException extends BaseRunTimeException{

    public DronePayloadDataManagementException(Exception exception) {
        super(exception);
    }

    public DronePayloadDataManagementException(String errorName) {
        super(errorName);
    }
}
