package com.musala.soft.drones.api.medicationRouter;

import com.musala.soft.drones.api.MedicationApi;
import com.musala.soft.drones.controller.medicationController.v1.MedicationController;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import com.musala.soft.drones.model.MedicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("MedicationApiRouterV1")
@RequestMapping("/api/v1")
public class MedicationApiRouter implements MedicationApi {

    @Autowired
    private MedicationController medicationController;

    @Override
    public ResponseEntity<MedicationResource> addMedication(MedicationDataTransferResource medicationDataTransferResource) {
        MedicationResource medicationResource = medicationController.addMedication(medicationDataTransferResource);
        return ResponseEntity.ok(medicationResource);
    }
}
