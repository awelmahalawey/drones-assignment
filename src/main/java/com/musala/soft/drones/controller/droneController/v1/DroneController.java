package com.musala.soft.drones.controller.droneController.v1;

import com.musala.soft.drones.controller.BaseController;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.DroneDataManagementException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.mapper.DroneDataMapper;
import com.musala.soft.drones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneController extends BaseController {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Autowired
    private DroneDataMapper droneDataMapper;

    public DroneResource addDrone(DroneDataTransferResource droneDataTransferResource) {
        try {
            Drone drone = droneDataManagementService.addDrone(droneDataTransferResource);

            return droneDataMapper.map(drone);
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public DroneResource updateDrone(String droneId,
                                     DroneDataTransferResource droneDataTransferResource) {
        try {
            Drone drone = droneDataManagementService.getDrone(droneId);

            drone = droneDataManagementService.updateDrone(drone, droneDataTransferResource);

            return droneDataMapper.map(drone);
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public List<DroneResource> fetchAllDrones(String searchText, DroneStateEnum state,
                                              DroneModelEnum model) {
        try {
            DroneState droneState = null;
            if(state != null) {
                droneState = DroneState.valueOf(state.getValue());
            }

            DroneModel droneModel = null;
            if(model != null) {
                droneModel = DroneModel.valueOf(model.getValue());
            }
            List<Drone> drones = droneDataManagementService.fetchDrones(searchText,
                    droneState, droneModel);

            return drones.stream().map(drone -> droneDataMapper.map(drone)).
                    collect(Collectors.toList());
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public DroneDetailedResource getDroneDetails(String droneId) {
        try {
            Drone drone = droneDataManagementService.getDrone(droneId);

            return droneDataMapper.mapDroneDetailedResource(drone);
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public BaseInfoResource deleteDrone(String droneId) {
        try {
            Drone drone = droneDataManagementService.getDrone(droneId);

            droneDataManagementService.deleteDrone(drone);

            BaseInfoResource baseInfoResource = new BaseInfoResource();
            baseInfoResource.setMessage("Drone Deleted Successfully");
            return baseInfoResource;
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }
}
