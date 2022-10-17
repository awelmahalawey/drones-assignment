package com.musala.soft.drones.dataService;

import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.exception.DroneDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.mapper.DroneDataMapper;
import com.musala.soft.drones.model.DroneDataTransferResource;
import com.musala.soft.drones.model.DroneModelEnum;
import com.musala.soft.drones.model.DroneStateEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class DroneDataManagementServiceTesting {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    private static String createdTestDroneId = "";

    @Autowired
    private DroneDataMapper droneDataMapper;

    @Order(0)
    @Test
    public void checkDroneFleetSize() {
        List<Drone> droneList = droneDataManagementService.fetchDrones("", null, null);
        assertEquals(droneList.size(), 10);
    }

    @Order(1)
    @Test
    public void checkDroneCreation_withMissingData() {
        DroneDataTransferResource droneDataTransferResource = new DroneDataTransferResource();
        droneDataTransferResource.setState(DroneStateEnum.IDLE);
        droneDataTransferResource.setModel(DroneModelEnum.LIGHT_WEIGHT);
        assertThrows(RequiredDataValidationException.class,
                () -> droneDataManagementService.addDrone(droneDataTransferResource));
    }

    @Order(2)
    @Test
    public void checkDroneCreation_withSuccess() {
        DroneDataTransferResource droneDataTransferResource = new DroneDataTransferResource();
        droneDataTransferResource.setState(DroneStateEnum.IDLE);
        droneDataTransferResource.setModel(DroneModelEnum.LIGHT_WEIGHT);
        droneDataTransferResource.setBatteryCap(12.0);
        droneDataTransferResource.setWeightLimit(300.0);
        droneDataTransferResource.setSerialNumber(UUID.randomUUID().toString());
        Drone drone = droneDataManagementService.addDrone(droneDataTransferResource);
        assertEquals(droneDataManagementService.fetchDrones("", null, null).size(),
                11);
        createdTestDroneId = drone.getId().toString();
    }

    @Order(3)
    @Test
    public void checkDroneGetDetailsById_withFailure() {
        assertThrows(DroneDataManagementException.class,
                () -> droneDataManagementService.getDrone(UUID.randomUUID().toString()));
    }

    @Order(4)
    @Test
    public void checkDroneGetDetailsById_withSuccess() {
        Drone drone = droneDataManagementService.getDrone(createdTestDroneId);
        assertNotNull(drone);
        assertEquals(drone.getId(),
                UUID.fromString(createdTestDroneId));
    }

    @Order(5)
    @Test
    public void checkDroneUpdate_withSuccess() {
        Drone drone = droneDataManagementService.getDrone(createdTestDroneId);
        Double oldBatteryCap = drone.getBatteryCap();
        DroneDataTransferResource droneDataTransferResource = droneDataMapper.mapDroneDataTransferResource(drone);
        droneDataTransferResource.setBatteryCap(30.0);
        drone = droneDataManagementService.updateDrone(drone, droneDataTransferResource);
        assertEquals(drone.getBatteryCap(), droneDataTransferResource.getBatteryCap());
        assertNotEquals(drone.getBatteryCap(), oldBatteryCap);
    }

    @Test
    @Order(6)
    public void checkDeleteDroneById_withSuccess() {
        Drone drone = droneDataManagementService.getDrone(createdTestDroneId);
        assertNotNull(drone);
        droneDataManagementService.deleteDrone(drone);
        assertEquals(droneDataManagementService.fetchDrones("", null, null).size(),
                10);
    }
}
