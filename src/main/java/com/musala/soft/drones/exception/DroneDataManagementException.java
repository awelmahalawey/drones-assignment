package com.musala.soft.drones.exception;

public class DroneDataManagementException extends BaseRunTimeException{

    public DroneDataManagementException(Exception exception) {
        super(exception);
    }

    public DroneDataManagementException(String errorName) {
        super(errorName);
    }
}
