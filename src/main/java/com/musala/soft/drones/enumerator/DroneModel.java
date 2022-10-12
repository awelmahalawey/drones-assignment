package com.musala.soft.drones.enumerator;

public enum DroneModel {
    LIGHT_WEIGHT("LightWeight"),
    MIDDLE_WEIGHT("MiddleWeight"),
    CRUISER_WEIGHT("CruiserWeight"),
    HEAVY_WEIGHT("HeavyWeight");

    private final String droneModel;

    DroneModel(String droneModel) {
        this.droneModel = droneModel;
    }

    public String getDroneModelValue() {
        return droneModel;
    }
}
