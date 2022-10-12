package com.musala.soft.drones.api.testRouter;

import com.musala.soft.drones.api.MockApi;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.model.BaseInfoResource;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("TestingControllerV1")
@RequestMapping("/api/v1")
public class TestingController implements MockApi {

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Override
    public ResponseEntity<BaseInfoResource> mockTestApi(){
        Page<Medication> medicationPage = medicationDataManagementService.fetchMedications("ol", PageRequest.of(0, 10));
        System.out.println(medicationPage.getTotalElements());
        Medication medication = medicationPage.stream().findFirst().orElse(null);
        if(medication != null) {
//            MedicationDataTransferResource medicationDataTransferResource = new MedicationDataTransferResource();
//            medicationDataTransferResource.setName("Prosaleen");
//            medicationDataTransferResource.setCode("CORA_222");
//            medicationDataTransferResource.setWeight(120.0);
            try {
                medicationDataManagementService.deleteMedication(medication);
            }
            catch (RequiredDataValidationException | MedicationDataManagementException ex) {
                ex.printStackTrace();
            }
        }
        BaseInfoResource baseInfoResource = new BaseInfoResource();
        baseInfoResource.setMessage("Done");
        return ResponseEntity.ok(baseInfoResource);
    }
}

