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
public class DroneShipmentInitializationTaskService {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Autowired
    private DronePayloadDataManagementService dronePayloadDataManagementService;

    @Transactional
    public void initializeReadyShipmentDrones() {
        List<Drone> drones = droneDataManagementService.fetchDrones("", DroneState.LOADED, null);
        drones.forEach(drone -> {
            droneDataManagementService.updateDroneState(drone, DroneState.DELIVERING);
            List<DronePayload> dronePayloads = dronePayloadDataManagementService.
                    fetchDronePayload(drone, PayloadState.READY_FOR_DELIVERY);
            if(dronePayloads.size() == 1) {
                dronePayloadDataManagementService.updateDronePayloadState(dronePayloads.get(0), PayloadState.IN_DELIVERY);
            }
        });
    }
}
