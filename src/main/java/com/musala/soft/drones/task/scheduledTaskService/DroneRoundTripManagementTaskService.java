package com.musala.soft.drones.task.scheduledTaskService;

import com.musala.soft.drones.constant.DroneConstants;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.dataService.DronePayloadDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.enumerator.PayloadState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class DroneRoundTripManagementTaskService {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Autowired
    private DronePayloadDataManagementService dronePayloadDataManagementService;

    @Transactional
    public void updateDeliveringDronesStatesUponReturn() {
        Long now = new Date().getTime();

        List<Drone> drones = droneDataManagementService.fetchDrones("", DroneState.DELIVERING, null);
        drones.forEach(drone -> {
            if(now - drone.getLastShipmentStartedAt() >= (DroneConstants.SHIPMENT_ROUND_TRIP_DURATION / 2)) {
                droneDataManagementService.updateDroneState(drone, DroneState.RETURNING);
                List<DronePayload> dronePayloads = dronePayloadDataManagementService.
                        fetchDronePayload(drone, List.of(PayloadState.IN_DELIVERY));
                if(dronePayloads.size() == 1) {
                    dronePayloadDataManagementService.updateDronePayloadState(dronePayloads.get(0), PayloadState.DELIVERED);
                }
            }
            droneDataManagementService.updateBatteryCap(drone, drone.getBatteryCap() - 1);
        });
    }

    @Transactional
    public void updateReturningDronesStatesUponArrival() {
        Long now = new Date().getTime();

        List<Drone> drones = droneDataManagementService.fetchDrones("", DroneState.RETURNING, null);
        drones.forEach(drone -> {
            if(now - drone.getLastShipmentStartedAt() >= DroneConstants.SHIPMENT_ROUND_TRIP_DURATION) {
                droneDataManagementService.updateDroneState(drone, DroneState.IDLE);
            }
            droneDataManagementService.updateBatteryCap(drone, drone.getBatteryCap() - 1);
        });
    }
}
