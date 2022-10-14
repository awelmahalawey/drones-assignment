package com.musala.soft.drones.entity;

import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.enumerator.PayloadType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "drone_payload")
public class DronePayload extends BaseEntityUUID {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drone_id")
    private Drone drone;

    @Column(name = "state", length = 255, nullable = false)
    @Enumerated(EnumType.STRING)
    private PayloadState state;

    @OneToMany(mappedBy = "dronePayload", fetch = FetchType.LAZY)
    private List<DronePayloadItem> dronePayloadItems;
}
