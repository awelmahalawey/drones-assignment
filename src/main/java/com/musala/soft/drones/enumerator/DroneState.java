package com.musala.soft.drones.enumerator;

public enum DroneState {
    IDLE("IDLE"),
    LOADING("LOADING"),
    LOADED("LOADED"),
    DELIVERING("DELIVERING"),
    DELIVERED("DELIVERED"),
    RETURNING("RETURNING");

    private final String droneState;

    DroneState(String droneState) {
        this.droneState = droneState;
    }

    public String getDroneState() {
        return droneState;
    }
}
