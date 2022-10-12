package com.musala.soft.drones.enumerator;

public enum PayloadState {
    READY_FOR_DELIVERY("READY_FOR_DELIVERY"),
    IN_DELIVERY("IN_DELIVERY"),
    DELIVERED("DELIVERED");

    private final String payloadState;

    PayloadState(String payloadState) {
        this.payloadState = payloadState;
    }

    public String getPayloadState() {
        return payloadState;
    }
}
