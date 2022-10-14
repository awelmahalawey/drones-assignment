package com.musala.soft.drones.controller.droneController.v1;

import com.musala.soft.drones.constant.DroneConstants;
import com.musala.soft.drones.constant.MessageConstants;
import com.musala.soft.drones.controller.BaseController;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.dataService.DronePayloadDataManagementService;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.exception.*;
import com.musala.soft.drones.mapper.DroneDataMapper;
import com.musala.soft.drones.mapper.DronePayloadDataMapper;
import com.musala.soft.drones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneController extends BaseController {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Autowired
    private DronePayloadDataManagementService dronePayloadDataManagementService;

    @Autowired
    private DroneDataMapper droneDataMapper;

    @Autowired
    private DronePayloadDataMapper dronePayloadDataMapper;

    @Transactional
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

    @Transactional
    public DroneResource updateDrone(String droneId,
                                     DroneDataTransferResource droneDataTransferResource) {
        try {
            Drone drone = droneDataManagementService.getDroneWithLock(droneId);

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

    public List<DroneResource> fetchAllEligibleDrones(Double shipmentWeight) {
        List<DroneResource> droneResources = fetchAllDrones("", DroneStateEnum.IDLE, null);

        if(shipmentWeight != null) {
            return droneResources.stream().filter(droneResource ->
                    droneResource.getWeightLimit() >= shipmentWeight).collect(Collectors.toList());
        }
        return droneResources;
    }

    public DronePayloadResource getDroneActivePayload(String droneId) {
        try {
            Drone drone = droneDataManagementService.getDrone(droneId);

            List<DronePayload> dronePayloads = dronePayloadDataManagementService.fetchDronePayload(drone,
                    List.of(PayloadState.READY_FOR_DELIVERY, PayloadState.IN_DELIVERY));
            if(!CollectionUtils.isEmpty(dronePayloads)) {
                return dronePayloadDataMapper.map(dronePayloads.get(0));
            }
            return null;
        }
        catch (DataValidationException | MedicationDataManagementException | DroneDataManagementException ex) {
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

    @Transactional
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

    @Transactional
    public DroneShipmentResource loadShipmentToDrone(
            DroneShipmentDataTransferResource droneShipmentDataTransferResource) {

        try {
            if(CollectionUtils.isEmpty(droneShipmentDataTransferResource.getPayloadItemsIdentifiers())) {
                throw apiResponseErrorException(
                        new DroneDataManagementException(MessageConstants.MISSING_PAYLOAD_ITEMS));
            }
            List<Medication> payloadItems = new ArrayList<>();
            Double totalPayloadItemsWeight = 0.0;
            for(String payloadItemIdentifier :
                    droneShipmentDataTransferResource.getPayloadItemsIdentifiers()) {
                Medication payload = medicationDataManagementService.getMedication(payloadItemIdentifier);
                totalPayloadItemsWeight += payload.getWeight();
                payloadItems.add(payload);
            }

            Drone drone;
            if(StringUtils.hasLength(droneShipmentDataTransferResource.getDroneId())) {
                drone = droneDataManagementService.getDroneWithLock(droneShipmentDataTransferResource.getDroneId());
                if(!drone.getState().equals(DroneState.IDLE)) {
                    throw apiResponseErrorException(
                            new DroneDataManagementException(MessageConstants.DRONE_STATE_INCAPABLE_FOR_SHIPMENT));
                }
                if(drone.getBatteryCap() < DroneConstants.MINIMUM_DRONE_BATTERY_CAP_FOR_SHIPMENT) {
                    throw apiResponseErrorException(
                            new DroneDataManagementException(MessageConstants.DRONE_BATTERY_CAP_INCAPABLE_FOR_SHIPMENT));
                }
                if(drone.getWeightLimit() < totalPayloadItemsWeight) {
                    throw apiResponseErrorException(
                            new DroneDataManagementException(MessageConstants.DRONE_WEIGHT_LIMIT_INCAPABLE_FOR_SHIPMENT));
                }
            }
            else {
                drone = droneDataManagementService.
                        findFirstDroneByStateAndBatteryCapMoreThanAndWeightLimitMoreThan(
                                DroneState.IDLE,
                                DroneConstants.MINIMUM_DRONE_BATTERY_CAP_FOR_SHIPMENT,
                                totalPayloadItemsWeight);

                if(drone == null) {
                    throw apiResponseErrorException(
                            new DroneDataManagementException(
                                    MessageConstants.NO_AVAILABLE_DRONES_FOR_SHIPMENT));
                }

                drone = droneDataManagementService.getDroneWithLock(drone.getId().toString());
            }

            DroneDataTransferResource droneDataTransferResource = droneDataMapper.mapDroneDataTransferResource(drone);
            droneDataTransferResource.setState(DroneStateEnum.LOADED);
            droneDataTransferResource.setLastShipmentStartedAt(new Date().getTime());
            drone = droneDataManagementService.updateDrone(drone, droneDataTransferResource);

            DronePayloadDataTransferResource dronePayloadDataTransferResource = new DronePayloadDataTransferResource();
            dronePayloadDataTransferResource.setState(PayloadStateEnum.READY_FOR_DELIVERY);
            List<DronePayloadItemDataTransferResource> dronePayloadItemDataTransferResources = payloadItems.stream().map(payload -> {
                DronePayloadItemDataTransferResource dronePayloadItemDataTransferResource = new DronePayloadItemDataTransferResource();
                dronePayloadItemDataTransferResource.setPayloadType(PayloadTypeEnum.MEDICATION);
                dronePayloadItemDataTransferResource.setPayloadIdentifier(payload.getId().toString());
                return dronePayloadItemDataTransferResource;
            }).collect(Collectors.toList());
            dronePayloadDataTransferResource.setPayloadItems(dronePayloadItemDataTransferResources);
            DronePayload dronePayload = dronePayloadDataManagementService.addDronePayload(drone, dronePayloadDataTransferResource);

            DroneShipmentResource droneShipmentResource = new DroneShipmentResource();
            droneShipmentResource.setDrone(droneDataMapper.map(drone));
            droneShipmentResource.setPayload(dronePayloadDataMapper.map(dronePayload));
            return droneShipmentResource;
        }
        catch (RequiredDataValidationException | DataValidationException | MedicationDataManagementException |
                DronePayloadDataManagementException | DroneDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }
}
