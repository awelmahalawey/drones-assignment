package com.musala.soft.drones.task.scheduledTask;

import com.musala.soft.drones.task.scheduledTaskService.DroneRoundTripManagementTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DroneRoundTripManagementTask {

    private final Logger logger = LoggerFactory.getLogger(DroneRoundTripManagementTask.class);

    @Autowired
    private DroneRoundTripManagementTaskService droneRoundTripManagementTaskService;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void droneRoundTripManagementTask() {
        droneRoundTripManagementTaskService.updateDeliveringDronesStatesUponReturn();
        droneRoundTripManagementTaskService.updateReturningDronesStatesUponArrival();
    }
}
