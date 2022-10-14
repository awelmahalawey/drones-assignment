package com.musala.soft.drones.mapper;

import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.entity.DronePayloadItem;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.model.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

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
        dronePayloadResource.setState(PayloadStateEnum.valueOf(dronePayload.getState().name()));
        if(!CollectionUtils.isEmpty(dronePayload.getDronePayloadItems())) {
            dronePayloadResource.setPayloadItems(dronePayload.getDronePayloadItems().stream().map(this::map).
                    collect(Collectors.toList()));
        }
        return dronePayloadResource;
    }

    public DronePayloadItemResource map(DronePayloadItem dronePayloadItem) {
        if(dronePayloadItem == null) {
            return null;
        }
        DronePayloadItemResource dronePayloadItemResource = this.modelMapper.map(dronePayloadItem,
                DronePayloadItemResource.class);
        dronePayloadItemResource.setPayloadType(PayloadTypeEnum.valueOf(dronePayloadItem.getType().name()));
        switch (dronePayloadItem.getType()) {
            case MEDICATION:
                Medication medication = medicationDataManagementService.
                        getMedication(dronePayloadItem.getPayloadIdentifier());
                dronePayloadItemResource.setPayloadDetails(medicationDataMapper.map(medication));
                break;
            default:
                break;
        }
        return dronePayloadItemResource;
    }
}
