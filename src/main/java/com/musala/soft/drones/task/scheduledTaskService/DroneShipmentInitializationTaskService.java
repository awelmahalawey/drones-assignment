package com.musala.soft.drones.task.scheduledTaskService;

import com.musala.soft.drones.constant.DroneConstants;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.enumerator.DroneState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DroneShipmentInitializationTaskService {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Transactional
    public void initializeReadyShipmentDrones() {
        List<Drone> drones = droneDataManagementService.fetchDrones("", DroneState.LOADED, null);
        drones.forEach(drone -> {
            droneDataManagementService.updateDroneState(drone, DroneState.DELIVERING);
        });
    }
}
