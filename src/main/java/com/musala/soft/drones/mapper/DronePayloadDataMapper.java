package com.musala.soft.drones.mapper;

import com.musala.soft.drones.dataService.DronePayloadDataManagementService;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class DronePayloadDataMapper {

    private ModelMapper modelMapper;

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Autowired
    private MedicationDataMapper medicationDataMapper;

    @Autowired
    private HttpServletRequest context;

    public DronePayloadDataMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public DronePayloadResource map(DronePayload dronePayload) throws DataValidationException,
            MedicationDataManagementException {
        if(dronePayload == null) {
            return null;
        }
        DronePayloadResource dronePayloadResource = this.modelMapper.map(dronePayload,
                DronePayloadResource.class);
        dronePayloadResource.setPayloadType(PayloadTypeEnum.valueOf(dronePayload.getType().name()));
        dronePayloadResource.setState(PayloadStateEnum.valueOf(dronePayload.getState().name()));
        switch (dronePayload.getType()) {
            case MEDICATION:
                Medication medication = medicationDataManagementService.
                        getMedication(dronePayload.getPayloadIdentifier());
                dronePayloadResource.setPayloadDetails(medicationDataMapper.map(medication));
                break;
            default:
                break;
        }
        return dronePayloadResource;
    }
}
