package com.musala.soft.drones.mapper;

import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.dataService.DronePayloadDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroneDataMapper {

    private ModelMapper modelMapper;

    @Autowired
    private DronePayloadDataManagementService dronePayloadDataManagementService;

    @Autowired
    private DronePayloadDataMapper dronePayloadDataMapper;

    @Autowired
    private HttpServletRequest context;

    public DroneDataMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public DroneResource map(Drone drone) throws DataValidationException,
            MedicationDataManagementException {
        if(drone == null) {
            return null;
        }
        DroneResource droneResource = this.modelMapper.map(drone, DroneResource.class);
        droneResource.setModel(DroneModelEnum.valueOf(drone.getModel().name()));
        droneResource.setState(DroneStateEnum.valueOf(drone.getState().name()));
        return droneResource;
    }

    public DroneDetailedResource mapDroneDetailedResource(Drone drone) throws DataValidationException,
            MedicationDataManagementException {
        if(drone == null) {
            return null;
        }
        DroneDetailedResource droneDetailedResource = this.modelMapper.map(drone,
                DroneDetailedResource.class);
        droneDetailedResource.setModel(DroneModelEnum.valueOf(drone.getModel().name()));
        droneDetailedResource.setState(DroneStateEnum.valueOf(drone.getState().name()));
        List<DronePayload> dronePayloads = drone.getDronePayloads();
        List<DronePayloadResource> dronePayloadResources = new ArrayList<>();
        if(dronePayloads != null) {
            dronePayloads.forEach(dronePayload -> {
                DronePayloadResource dronePayloadResource = dronePayloadDataMapper.map(dronePayload);
                dronePayloadResources.add(dronePayloadResource);
            });
        }
        droneDetailedResource.setPayloads(dronePayloadResources);
        return droneDetailedResource;
    }

    public DroneDataTransferResource mapDroneDataTransferResource(Drone drone) throws DataValidationException,
            MedicationDataManagementException {
        if(drone == null) {
            return null;
        }
        DroneDataTransferResource droneDataTransferResource = this.modelMapper.map(drone,
                DroneDataTransferResource.class);
        droneDataTransferResource.setModel(DroneModelEnum.valueOf(drone.getModel().name()));
        droneDataTransferResource.setState(DroneStateEnum.valueOf(drone.getState().name()));
        return droneDataTransferResource;
    }
}
