package com.musala.soft.drones.dataService;

import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.mapper.DroneDataMapper;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class medicationDataManagementServiceTesting {

    @Autowired
    private MedicationDataManagementService medicationDataManagementService;

    @Autowired
    private DroneDataMapper droneDataMapper;

    @Order(0)
    @Test
    public void checkMedicationSize() {
        Page<Medication> medicationPage = medicationDataManagementService.fetchMedications("", null);
        assertEquals(medicationPage.getTotalElements(), 7);
    }

    @Order(1)
    @Test
    public void checkMedicationCreation_withMissingData() {
        MedicationDataTransferResource medicationDataTransferResource = new MedicationDataTransferResource();
        medicationDataTransferResource.setName("Testing Med");
        medicationDataTransferResource.setImageUrl("Image.png");
        assertThrows(RequiredDataValidationException.class,
                () -> medicationDataManagementService.addMedication(medicationDataTransferResource));
    }

    @Order(2)
    @Test
    public void checkMedicationCreation_withInvalidData() {
        MedicationDataTransferResource medicationDataTransferResource = new MedicationDataTransferResource();
        medicationDataTransferResource.setName("Testing Med#");
        medicationDataTransferResource.setCode("TestingMed-66");
        medicationDataTransferResource.setWeight(343.0);
        medicationDataTransferResource.setImageUrl("Image.png");
        assertThrows(MedicationDataManagementException.class,
                () -> medicationDataManagementService.addMedication(medicationDataTransferResource));
    }

    @Order(3)
    @Test
    public void checkMedicationCreation_withSuccess() {
        MedicationDataTransferResource medicationDataTransferResource = new MedicationDataTransferResource();
        medicationDataTransferResource.setName("TestingMed");
        medicationDataTransferResource.setCode("TST_66");
        medicationDataTransferResource.setWeight(343.0);
        medicationDataTransferResource.setImageUrl("Image.png");
        medicationDataManagementService.addMedication(medicationDataTransferResource);
        assertEquals(medicationDataManagementService.fetchMedications("", null).getTotalElements(),
                8);
    }

    @Order(5)
    @Test
    public void checkMedicationUpdate_withSuccess() {
        Medication medication = medicationDataManagementService.getMedication("55e74b88-4f97-4fc5-b0ef-f9cd7dc5ff4f");
        Double oldWeight = medication.getWeight();
        MedicationDataTransferResource medicationDataTransferResource = new MedicationDataTransferResource();
        medicationDataTransferResource.setName(medication.getName());
        medicationDataTransferResource.setCode(medication.getCode());
        medicationDataTransferResource.setImageUrl(medication.getImageUrl());
        medicationDataTransferResource.setWeight(100.0);
        medication = medicationDataManagementService.updateMedication(medication, medicationDataTransferResource);
        assertEquals(medication.getWeight(), medicationDataTransferResource.getWeight());
        assertNotEquals(medication.getWeight(), oldWeight);
    }

    @Test
    @Order(6)
    public void checkDeleteMedicationById_withSuccess() {

        Medication medication = medicationDataManagementService.getMedication("55e74b88-4f97-4fc5-b0ef-f9cd7dc5ff4f");
        assertNotNull(medication);
        medicationDataManagementService.deleteMedication(medication);
        assertEquals(medicationDataManagementService.fetchMedications("", null).getTotalElements(),
                7);
    }
}
