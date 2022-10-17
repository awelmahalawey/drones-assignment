package com.musala.soft.drones.task.scheduledTaskService;

import com.musala.soft.drones.constant.DroneConstants;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.task.scheduledTask.DroneRoundTripManagementTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DroneBatteryCapManagementTaskService {

    private final Logger logger = LoggerFactory.getLogger(DroneBatteryCapManagementTaskService.class);

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Transactional
    public void chargeIdleDrones() {
        Long now = new Date().getTime();

        List<Drone> drones = droneDataManagementService.fetchDrones("", DroneState.IDLE, null);
        drones.forEach(drone -> {
            if(drone.getBatteryCap() < 100) {
                Double newBatteryCap = Math.min(100, drone.getBatteryCap() + DroneConstants.IDLE_DRONE_CHARGING_RATE_PER_SECOND);
                droneDataManagementService.updateBatteryCap(drone, newBatteryCap);
                if(newBatteryCap.equals(100.0)) {
                    logger.info("Drone (s/n): '" + drone.getSerialNumber() +
                            "', fully charged.");
                }
                else {
                    logger.info("Drone (s/n): '" + drone.getSerialNumber() +
                            "', charging, batteryCap: " + newBatteryCap + "%.");
                }
            }
        });
    }
}
