package com.musala.soft.drones.api.testRouter;

import com.musala.soft.drones.api.MockApi;
import com.musala.soft.drones.dataService.DroneDataManagementService;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.exception.DroneDataManagementException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController("TestingControllerV1")
@RequestMapping("/api/v1")
public class TestingController implements MockApi {

    @Autowired
    private DroneDataManagementService droneDataManagementService;

    @Override
    public ResponseEntity<BaseInfoResource> mockTestApi(){
        List<Drone> drones = droneDataManagementService.fetchDrones("", null, null);
        System.out.println(drones.size());

        drones = droneDataManagementService.fetchDrones("", DroneState.IDLE, null);
        System.out.println(drones.size());

        drones = droneDataManagementService.fetchDrones("", DroneState.LOADED, null);
        System.out.println(drones.size());

        drones = droneDataManagementService.fetchDrones("", DroneState.IDLE, DroneModel.LIGHT_WEIGHT);
        System.out.println(drones.size());
        BaseInfoResource baseInfoResource = new BaseInfoResource();
        baseInfoResource.setMessage("Done");
        return ResponseEntity.ok(baseInfoResource);
    }
}

