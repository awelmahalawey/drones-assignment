package com.musala.soft.drones.controller.medicationController.v1;

import com.musala.soft.drones.controller.BaseController;
import com.musala.soft.drones.dataService.MedicationDataManagementService;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.mapper.MedicationDataMapper;
import com.musala.soft.drones.model.BaseInfoResource;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import com.musala.soft.drones.model.MedicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Locale;

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

    public MedicationResource updateMedication(String medicationId, MedicationDataTransferResource medicationDataTransferResource) {

        try {
            Medication medication = medicationDataManagementService.
                    getMedication(medicationId);

            medication = medicationDataManagementService.
                    updateMedication(medication, medicationDataTransferResource);

            return medicationDataMapper.map(medication);
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public Page<MedicationResource> fetchMedications(String searchText, Pageable pageable) {

        try {
            Page<Medication> medications = medicationDataManagementService.
                    fetchMedications(searchText, pageable);

            return medications.map(medication -> medicationDataMapper.map(medication));
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }

    public BaseInfoResource deleteMedication(String medicationId) {

        try {
            Medication medication = medicationDataManagementService.
                    getMedication(medicationId);

            medicationDataManagementService.
                    deleteMedication(medication);

            BaseInfoResource baseInfoResource = new BaseInfoResource();
            baseInfoResource.setMessage("Medication Deleted Successfully");
            return baseInfoResource;
        }
        catch (RequiredDataValidationException | DataValidationException |
                MedicationDataManagementException ex) {
            throw apiResponseErrorException(ex);
        }
    }
}
