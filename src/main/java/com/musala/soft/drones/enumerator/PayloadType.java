package com.musala.soft.drones.enumerator;

public enum PayloadType {
    MEDICATION("MEDICATION");

    private final String payloadType;

    PayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public String getPayloadType() {
        return payloadType;
    }
}
