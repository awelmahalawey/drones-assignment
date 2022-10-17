package com.musala.soft.drones.dataService;

import com.musala.soft.drones.constant.MessageConstants;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
import com.musala.soft.drones.entity.DronePayloadItem;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.PayloadState;
import com.musala.soft.drones.enumerator.PayloadType;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.DronePayloadDataManagementException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.model.DronePayloadDataTransferResource;
import com.musala.soft.drones.model.DronePayloadItemDataTransferResource;
import com.musala.soft.drones.repository.DronePayloadItemRepository;
import com.musala.soft.drones.repository.DronePayloadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.musala.soft.drones.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DronePayloadDataManagementService {

    @Autowired
    private DronePayloadRepository dronePayloadRepository;

    @Autowired
    private DronePayloadItemRepository dronePayloadItemRepository;

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Transactional
    public DronePayload addDronePayload(Drone drone,
                                        DronePayloadDataTransferResource dronePayloadDataTransferResource)
            throws RequiredDataValidationException, DataValidationException, MedicationDataManagementException,
            DronePayloadDataManagementException {

        ValidationUtil.validateRequiredData(dronePayloadDataTransferResource);

        if (drone == null) {
            throw new RequiredDataValidationException(MessageConstants.MISSING_DRONE_FOR_PAYLOAD);
        }
        validatePayloadItems(dronePayloadDataTransferResource.getPayloadItems());
        DronePayload dronePayload = new DronePayload();
        return setDataAndSave(dronePayload, drone, dronePayloadDataTransferResource);
    }

    @Transactional
    public DronePayload updateDronePayload(DronePayload dronePayload, Drone drone,
                                           DronePayloadDataTransferResource dronePayloadDataTransferResource)
            throws RequiredDataValidationException, DataValidationException, MedicationDataManagementException,
            DronePayloadDataManagementException {

        ValidationUtil.validateRequiredData(dronePayloadDataTransferResource);

        if (drone == null) {
            throw new RequiredDataValidationException(MessageConstants.MISSING_DRONE_FOR_PAYLOAD);
        }
        validatePayloadItems(dronePayloadDataTransferResource.getPayloadItems());
        return setDataAndSave(dronePayload, drone, dronePayloadDataTransferResource);
    }

    @Transactional
    public DronePayload updateDronePayloadState(DronePayload dronePayload, PayloadState payloadState)
            throws RequiredDataValidationException, DataValidationException, MedicationDataManagementException,
            DronePayloadDataManagementException {

        if (dronePayload == null) {
            throw new DronePayloadDataManagementException(MessageConstants.DRONE_PAYLOAD_NOT_FOUND);
        }
        dronePayload.setState(payloadState);
        return dronePayloadRepository.save(dronePayload);
    }

    @Transactional
    public void deleteDronePayload(DronePayload dronePayload)
            throws DronePayloadDataManagementException {

        if (dronePayload == null) {
            throw new DronePayloadDataManagementException(MessageConstants.DRONE_PAYLOAD_NOT_FOUND);
        }
        dronePayload.setIsActive(false);
        dronePayloadRepository.save(dronePayload);
    }

    public DronePayload getDronePayload(String dronePayloadId)
            throws DataValidationException, DronePayloadDataManagementException {

        if (StringUtils.hasLength(dronePayloadId)) {
            throw new DataValidationException(MessageConstants.INVALID_DRONE_PAYLOAD_ID);
        }

        DronePayload dronePayload = dronePayloadRepository.findByIdAndIsActive(UUID.fromString(dronePayloadId), true);
        if (dronePayload == null) {
            throw new DronePayloadDataManagementException(MessageConstants.DRONE_PAYLOAD_NOT_FOUND);
        }
        return dronePayload;
    }

    public List<DronePayload> fetchDronePayload(Drone drone, List<PayloadState> payloadStates) {
        return dronePayloadRepository.findAllWithFilter(drone, !CollectionUtils.isEmpty(payloadStates),
                payloadStates);
    }

    private void validatePayloadItems(List<DronePayloadItemDataTransferResource> dronePayloadItemDataTransferResources)
            throws DataValidationException, MedicationDataManagementException,
            DronePayloadDataManagementException {
        if (CollectionUtils.isEmpty(dronePayloadItemDataTransferResources)) {
            throw new DronePayloadDataManagementException(MessageConstants.MISSING_PAYLOAD_ITEMS);
        }
        for (DronePayloadItemDataTransferResource dronePayloadItemDataTransferResource :
                dronePayloadItemDataTransferResources) {
            switch (dronePayloadItemDataTransferResource.getPayloadType()) {
                case MEDICATION:
                    Medication medicationPackage = medicationDataManagementService.getMedication
                            (dronePayloadItemDataTransferResource.getPayloadIdentifier());
                    if (medicationPackage == null) {
                        throw new DronePayloadDataManagementException(
                                MessageConstants.INVALID_PAYLOAD_IDENTIFIER);
                    }
                    break;
                default:
                    throw new DronePayloadDataManagementException(MessageConstants.INVALID_PAYLOAD_TYPE);
            }
        }
    }

    private DronePayload setDataAndSave(
            DronePayload dronePayload,
            Drone drone,
            DronePayloadDataTransferResource dronePayloadDataTransferResource
    ) {
        dronePayload.setState(PayloadState.valueOf(dronePayloadDataTransferResource.getState().getValue()));
        dronePayload.setDrone(drone);
        dronePayload.setIsActive(true);
        dronePayload = dronePayloadRepository.save(dronePayload);
        List<DronePayloadItem> dronePayloadItems = new ArrayList<>();
        for(DronePayloadItemDataTransferResource dronePayloadItemDataTransferResource :
                dronePayloadDataTransferResource.getPayloadItems()) {
            DronePayloadItem dronePayloadItem = new DronePayloadItem();
            dronePayloadItem.setDronePayload(dronePayload);
            dronePayloadItem.setType(PayloadType.valueOf(dronePayloadItemDataTransferResource.getPayloadType().getValue()));
            dronePayloadItem.setPayloadIdentifier(dronePayloadItemDataTransferResource.getPayloadIdentifier());
            dronePayloadItem.setIsActive(true);
            dronePayloadItems.add(dronePayloadItemRepository.save(dronePayloadItem));
        }
        dronePayload.setDronePayloadItems(dronePayloadItems);
        return dronePayload;
    }
}
