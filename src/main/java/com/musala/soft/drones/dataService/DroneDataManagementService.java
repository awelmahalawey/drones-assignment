package com.musala.soft.drones.dataService;

import com.musala.soft.drones.constant.MessageConstants;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.DroneDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.model.DroneDataTransferResource;
import com.musala.soft.drones.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.ValidationUtil;

import java.util.List;
import java.util.UUID;

@Service
public class DroneDataManagementService {

    @Autowired
    private DroneRepository droneRepository;

    @Transactional
    public Drone addDrone(DroneDataTransferResource droneDataTransferResource)
            throws RequiredDataValidationException, DroneDataManagementException {

        ValidationUtil.validateRequiredData(droneDataTransferResource);
        validateDroneDataConstraints(droneDataTransferResource);

        Drone drone = droneRepository.findBySerialNumber(droneDataTransferResource.getSerialNumber());
        if (drone != null) {
            if (drone.getIsActive()) {
                throw new DroneDataManagementException(MessageConstants.DRONE_ALREADY_EXISTS);
            }
        } else {
            drone = new Drone();
        }
        return setDataAndSave(drone, droneDataTransferResource);
    }

    @Transactional
    public Drone updateDrone(Drone drone,
                                  DroneDataTransferResource droneDataTransferResource)
            throws RequiredDataValidationException, DroneDataManagementException {

        ValidationUtil.validateRequiredData(droneDataTransferResource);
        validateDroneDataConstraints(droneDataTransferResource);

        Drone existingDrone = droneRepository.findBySerialNumber(droneDataTransferResource.getSerialNumber());

        if (existingDrone != null && !existingDrone.getId().equals(drone.getId())) {
            throw new DroneDataManagementException(MessageConstants.DRONE_ALREADY_EXISTS);
        }
        return setDataAndSave(drone, droneDataTransferResource);
    }

    @Transactional
    public void deleteDrone(Drone drone)
            throws DroneDataManagementException {

        if (drone == null) {
            throw new DroneDataManagementException(MessageConstants.DRONE_NOT_FOUND);
        }
        drone.setIsActive(false);
        droneRepository.save(drone);
    }

    public Drone getDrone(String droneId)
            throws DataValidationException, DroneDataManagementException {

        if (StringUtils.hasLength(droneId)) {
            throw new DataValidationException(MessageConstants.INVALID_DRONE_ID);
        }

        Drone drone = droneRepository.findByIdAndIsActive(UUID.fromString(droneId), true);
        if (drone == null) {
            throw new DroneDataManagementException(MessageConstants.DRONE_NOT_FOUND);
        }
        return drone;
    }

    public List<Drone> fetchDrones(String searchText, DroneState droneState, DroneModel droneModel) {
        return droneRepository.findAllWithFilters(
                StringUtils.hasLength(searchText) ? searchText.toLowerCase() : "",
                droneState != null, droneState, droneModel != null, droneModel);
    }

    private void validateDroneDataConstraints (DroneDataTransferResource droneDataTransferResource)
            throws DroneDataManagementException {
        if(droneDataTransferResource.getSerialNumber().length() > 100) {
            throw new DroneDataManagementException(MessageConstants.INVALID_DRONE_SERIAL_NUMBER);
        }
        if(droneDataTransferResource.getWeightLimit() > 500 || droneDataTransferResource.getWeightLimit() < 0) {
            throw new DroneDataManagementException(MessageConstants.INVALID_DRONE_WEIGHT_LIMIT);
        }
        if(droneDataTransferResource.getBatteryCap() > 100 || droneDataTransferResource.getBatteryCap() < 0) {
            throw new DroneDataManagementException(MessageConstants.INVALID_DRONE_BATTERY_CAP);
        }
    }

    private Drone setDataAndSave(
            Drone drone,
            DroneDataTransferResource droneDataTransferResource
    ) {
        drone.setSerialNumber(droneDataTransferResource.getSerialNumber());
        drone.setState(DroneState.valueOf(droneDataTransferResource.getState().getValue()));
        drone.setModel(DroneModel.valueOf(droneDataTransferResource.getModel().getValue()));
        drone.setWeightLimit(droneDataTransferResource.getWeightLimit());
        drone.setBatteryCap(droneDataTransferResource.getBatteryCap());
        drone.setIsActive(true);
        return droneRepository.save(drone);
    }
}
