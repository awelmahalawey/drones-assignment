package com.musala.soft.drones.entity;

import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "drone")
public class Drone extends BaseEntityUUID {

    @Column(name = "serial_number", length = 100, nullable = false)
    private String serialNumber;

    @Column(name = "model", length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    private DroneModel model;

    @Column(name = "state", length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    private DroneState state;

    @Column(name = "weight_limit", nullable = false)
    private Double weightLimit;

    @Column(name = "battery_cap", nullable = false)
    private Double batteryCap;

    @OneToMany(mappedBy = "drone", fetch = FetchType.LAZY)
    private List<DronePayload> dronePayloads;
}
