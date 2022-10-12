package com.musala.soft.drones.exception;

import com.musala.soft.drones.constant.MessageConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequiredDataValidationException extends BaseRunTimeException {
    private List<String> violationErrorMessages;

    public RequiredDataValidationException(List<String> errorsList) {
        super(MessageConstants.MISSING_REQUIRED_DATA);
        this.setMessage(Arrays.toString(errorsList.toArray()));
        this.violationErrorMessages = new ArrayList<>(errorsList);
    }

    public RequiredDataValidationException(String errorName) {
        super(errorName);
    }
}
