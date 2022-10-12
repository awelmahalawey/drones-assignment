package com.musala.soft.drones.exception;

public class MedicationDataManagementException extends BaseRunTimeException{

    public MedicationDataManagementException(Exception exception) {
        super(exception);
    }

    public MedicationDataManagementException(String errorName) {
        super(errorName);
    }
}
