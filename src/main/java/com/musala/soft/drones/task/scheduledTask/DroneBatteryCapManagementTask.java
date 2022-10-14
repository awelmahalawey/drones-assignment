package com.musala.soft.drones.task.scheduledTask;

import com.musala.soft.drones.task.scheduledTaskService.DroneBatteryCapManagementTaskService;
import com.musala.soft.drones.task.scheduledTaskService.DroneRoundTripManagementTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DroneBatteryCapManagementTask {

    private final Logger logger = LoggerFactory.getLogger(DroneRoundTripManagementTask.class);

    @Autowired
    private DroneBatteryCapManagementTaskService droneBatteryCapManagementTaskService;

    @Transactional
    @Scheduled(cron = "* * * * * *")
    public void droneRoundTripManagementTask() {
        System.out.println(new Date());
        logger.info("******************* Drone Battery Cap Management Cron Job *******************");
        logger.info("****************************** Start ******************************");

        droneBatteryCapManagementTaskService.chargeIdleDrones();

        logger.info("******************* Drone Battery Cap Management Cron Job *******************");
        logger.info("******************************* End *******************************");
    }
}
