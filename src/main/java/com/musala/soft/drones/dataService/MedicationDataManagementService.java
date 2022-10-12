package com.musala.soft.drones.dataService;

import com.musala.soft.drones.constant.MessageConstants;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.exception.DataValidationException;
import com.musala.soft.drones.exception.MedicationDataManagementException;
import com.musala.soft.drones.exception.RequiredDataValidationException;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import com.musala.soft.drones.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.ValidationUtil;

import java.util.UUID;

@Service
public class MedicationDataManagementService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Transactional
    public Medication addMedication(MedicationDataTransferResource medicationDataTransferResource)
            throws RequiredDataValidationException, MedicationDataManagementException {

        ValidationUtil.validateRequiredData(medicationDataTransferResource);
        validateMedicationDataConstraints(medicationDataTransferResource);

        Medication medication = medicationRepository.findByNameAndCode(medicationDataTransferResource.getName(),
                medicationDataTransferResource.getCode());
        if (medication != null) {
            if (medication.getIsActive()) {
                throw new MedicationDataManagementException(MessageConstants.MEDICATION_ALREADY_EXIST);
            }
        } else {
            medication = new Medication();
        }
        return setDataAndSave(medication, medicationDataTransferResource);
    }

    @Transactional
    public Medication updateMedication(Medication medication,
                                       MedicationDataTransferResource medicationDataTransferResource)
            throws RequiredDataValidationException, MedicationDataManagementException {

        ValidationUtil.validateRequiredData(medicationDataTransferResource);
        validateMedicationDataConstraints(medicationDataTransferResource);

        Medication existingMedication = medicationRepository.findByNameAndCode(medicationDataTransferResource.getName(),
                medicationDataTransferResource.getCode());

        if (existingMedication != null && !existingMedication.getId().equals(medication.getId())) {
            throw new MedicationDataManagementException(MessageConstants.MEDICATION_ALREADY_EXIST);
        }
        return setDataAndSave(medication, medicationDataTransferResource);
    }

    @Transactional
    public void deleteMedication(Medication medication)
            throws RequiredDataValidationException, MedicationDataManagementException {

        if (medication == null) {
            throw new MedicationDataManagementException(MessageConstants.MEDICATION_NOT_FOUND);
        }
        medication.setIsActive(false);
        medicationRepository.save(medication);
    }

    public Medication getMedication(String medicationId)
            throws DataValidationException, MedicationDataManagementException {

        if (StringUtils.hasLength(medicationId)) {
            throw new DataValidationException(MessageConstants.INVALID_MEDICATION_ID);
        }

        Medication medication = medicationRepository.findByIdAndIsActive(UUID.fromString(medicationId), true);
        if (medication == null) {
            throw new MedicationDataManagementException(MessageConstants.MEDICATION_NOT_FOUND);
        }
        return medication;
    }

    public Page<Medication> fetchMedications(String searchText, Pageable pageable) {

        return medicationRepository.findAllWithFilters(
                StringUtils.hasLength(searchText) ? searchText.toLowerCase() : "",
                pageable);

    }

    private void validateMedicationDataConstraints (MedicationDataTransferResource
                                                            medicationDataTransferResource)
            throws MedicationDataManagementException {
        String namePattern = "([a-zA-Z0-9_-])+";
        if(!medicationDataTransferResource.getName().matches(namePattern)) {
            throw new MedicationDataManagementException(MessageConstants.INVALID_MEDICATION_NAME);
        }
        String codePattern = "([A-Z0-9_])+";
        if(!medicationDataTransferResource.getCode().matches(codePattern)) {
            throw new MedicationDataManagementException(MessageConstants.INVALID_MEDICATION_CODE);
        }
    }

    private Medication setDataAndSave(
            Medication medication,
            MedicationDataTransferResource medicationDataTransferResource
    ) {
        medication.setName(medicationDataTransferResource.getName());
        medication.setCode(medicationDataTransferResource.getCode());
        medication.setWeight(medicationDataTransferResource.getWeight());
        medication.setImageUrl(medicationDataTransferResource.getImageUrl());
        medication.setIsActive(true);
        return medicationRepository.save(medication);
    }
}
