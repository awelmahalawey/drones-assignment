package com.musala.soft.drones.controller.medicationController.v1;

import com.musala.soft.drones.controller.BaseController;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.mapper.MedicationDataMapper;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import com.musala.soft.drones.model.MedicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MedicationController extends BaseController {

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Autowired
    private MedicationDataMapper medicationDataMapper;

    public MedicationResource addMedication(MedicationDataTransferResource medicationDataTransferResource) {

        try {
            Medication medication = medicationDataManagementService.
                    addMedication(medicationDataTransferResource);

            return medicationDataMapper.map(medication);
        }
        catch (RequiredDataValidationException | MedicationDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }
}
