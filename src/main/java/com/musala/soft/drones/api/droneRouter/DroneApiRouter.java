package com.musala.soft.drones.api.droneRouter;

import com.musala.soft.drones.api.DroneApi;
import com.musala.soft.drones.constant.WebConstants;
import com.musala.soft.drones.controller.droneController.v1.DroneController;
import com.musala.soft.drones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("DroneApiRouterV1")
@RequestMapping("/api/v1")
public class DroneApiRouter implements DroneApi {

    @Autowired
    private DroneController droneController;

    @Override
    public ResponseEntity<DroneResource> addDrone(DroneDataTransferResource droneDataTransferResource) {
        DroneResource droneResource = droneController.addDrone(droneDataTransferResource);
        return ResponseEntity.ok(droneResource);
    }

    @Override
    public ResponseEntity<DroneResource> updateDrone(String droneId,
                                                     DroneDataTransferResource droneDataTransferResource) {
        DroneResource droneResource = droneController.updateDrone(droneId, droneDataTransferResource);
        return ResponseEntity.ok(droneResource);
    }

    @Override
    public ResponseEntity<List<DroneResource>> fetchAllDrones(String searchText,
                                                              DroneStateEnum state, DroneModelEnum model) {
        List<DroneResource> droneResources =
                droneController.fetchAllDrones(searchText, state, model);
        return ResponseEntity.ok()
                .header(WebConstants.HEADER_KEY_PAGINATION_TOTAL_COUNT,
                        String.valueOf(droneResources.size()))
                .header(WebConstants.HEADER_KEY_PAGINATION_CURRENT_PAGE, String.valueOf(0))
                .header(WebConstants.HEADER_KEY_PAGINATION_HAS_MORE, String.valueOf(false))
                .body(droneResources);
    }

    @Override
    public ResponseEntity<DroneDetailedResource> getDroneDetails(String droneId) {
        DroneDetailedResource droneDetailedResource = droneController.getDroneDetails(droneId);
        return ResponseEntity.ok(droneDetailedResource);
    }

    @Override
    public ResponseEntity<BaseInfoResource> deleteDrone(String droneId) {
        BaseInfoResource baseInfoResource = droneController.deleteDrone(droneId);
        return ResponseEntity.ok(baseInfoResource);
    }

    @Override
    public ResponseEntity<DroneShipmentResource> loadShipmentToDrone(
            DroneShipmentDataTransferResource droneShipmentDataTransferResource) {
        DroneShipmentResource droneShipmentResource = droneController.loadShipmentToDrone(droneShipmentDataTransferResource);
        return ResponseEntity.ok(droneShipmentResource);
    }
}
