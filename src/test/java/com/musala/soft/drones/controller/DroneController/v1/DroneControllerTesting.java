package com.musala.soft.drones.controller.DroneController.v1;

import com.musala.soft.drones.controller.droneController.v1.DroneController;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.ApiResponseErrorException;
import com.musala.soft.drones.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class DroneControllerTesting {

    @Autowired
    private DroneController droneController;

    private static String createdTestDroneId = "";

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Order(0)
    @Test
    public void checkDroneFleetSize() {
        List<DroneResource> droneList = droneController.fetchAllDrones("", null, null);
        assertEquals(droneList.size(), 10);
    }

    @Order(1)
    @Test
    public void checkDroneCreation_withMissingData() {
        DroneDataTransferResource droneDataTransferResource = new DroneDataTransferResource();
        droneDataTransferResource.setState(DroneStateEnum.IDLE);
        droneDataTransferResource.setModel(DroneModelEnum.LIGHT_WEIGHT);
        assertThrows(ApiResponseErrorException.class,
                () -> droneController.addDrone(droneDataTransferResource));
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
        DroneResource droneResource = droneController.addDrone(droneDataTransferResource);
        assertEquals(droneController.fetchAllDrones("", null, null).size(),
                11);
        createdTestDroneId = droneResource.getId();
        System.out.println("Drone ID Setting:" +  createdTestDroneId);
    }

    @Order(3)
    @Test
    public void checkDroneGetDetailsById_withFailure() {
        assertThrows(ApiResponseErrorException.class,
                () -> droneController.getDroneDetails(UUID.randomUUID().toString()));
    }

    @Order(4)
    @Test
    public void checkDroneGetDetailsById_withSuccess() {
        System.out.println("Drone ID: " + createdTestDroneId);
        DroneDetailedResource droneDetailedResource = droneController.getDroneDetails(createdTestDroneId);
        assertNotNull(droneDetailedResource);
        assertEquals(droneDetailedResource.getId(), createdTestDroneId);
    }

    @Order(5)
    @Test
    public void checkDroneUpdate_withSuccess() {
        DroneDetailedResource droneDetailedResource = droneController.getDroneDetails(createdTestDroneId);
        Double oldBatteryCap = droneDetailedResource.getBatteryCap();
        DroneDataTransferResource droneDataTransferResource = new DroneDataTransferResource();
        droneDataTransferResource.setSerialNumber(droneDetailedResource.getSerialNumber());
        droneDataTransferResource.setModel(droneDetailedResource.getModel());
        droneDataTransferResource.setState(droneDetailedResource.getState());
        droneDataTransferResource.setWeightLimit(droneDetailedResource.getWeightLimit());
        droneDataTransferResource.setBatteryCap(30.0);
        DroneResource droneResource = droneController.updateDrone(createdTestDroneId, droneDataTransferResource);
        assertEquals(droneResource.getBatteryCap(), droneDataTransferResource.getBatteryCap());
        assertNotEquals(droneResource.getBatteryCap(), oldBatteryCap);
    }

    @Test
    @Order(6)
    public void checkDeleteDroneById_withSuccess() {
        System.out.println("Drone ID: " + createdTestDroneId);
        droneController.deleteDrone(createdTestDroneId);
        assertEquals(droneController.fetchAllDrones("", null, null).size(),
                10);
    }

    @Test
    @Order(7)
    public void checkLoadDrone_withFailure() {
        List<Medication> medications = medicationDataManagementService.fetchMedications("", null).getContent();
        List<String> medicationsIds = medications.stream().map(s -> s.getId().toString()).collect(Collectors.toList());
        double totalWeight = medications.stream().mapToDouble(Medication::getWeight).sum();
        while(totalWeight < 500) {
            medicationsIds.addAll(medications.stream().map(s -> s.getId().toString()).collect(Collectors.toList()));
            totalWeight += medications.stream().mapToDouble(Medication::getWeight).sum();
        }
        System.out.println(totalWeight);
        DroneShipmentDataTransferResource droneShipmentDataTransferResource = new DroneShipmentDataTransferResource();
        droneShipmentDataTransferResource.setPayloadItemsIdentifiers(medicationsIds);
        assertThrows(ApiResponseErrorException.class,
                () -> droneController.loadShipmentToDrone(droneShipmentDataTransferResource));
    }

    @Test
    @Order(8)
    public void checkLoadDrone_withSuccess() {
        List<Medication> medications = medicationDataManagementService.fetchMedications("", null).getContent();
        List<String> medicationsIds = new ArrayList<>();
        double totalWeight = 0;
        for (Medication medication : medications) {
            if(totalWeight + medication.getWeight() < 500) {
                medicationsIds.add(medication.getId().toString());
                totalWeight += medication.getWeight();
            }
        }
        int availableDronesForLoadingCountBefore = droneController.fetchAllEligibleDrones(totalWeight).size();
        DroneShipmentDataTransferResource droneShipmentDataTransferResource = new DroneShipmentDataTransferResource();
        droneShipmentDataTransferResource.setPayloadItemsIdentifiers(medicationsIds);
        droneController.loadShipmentToDrone(droneShipmentDataTransferResource);
        Integer availableDronesForLoadingCountAfter = droneController.fetchAllEligibleDrones(totalWeight).size();
        assertEquals(availableDronesForLoadingCountBefore - 1, availableDronesForLoadingCountAfter);
    }
}
