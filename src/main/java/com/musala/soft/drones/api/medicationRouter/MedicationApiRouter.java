package com.musala.soft.drones.api.medicationRouter;

import com.musala.soft.drones.api.MedicationApi;
import com.musala.soft.drones.constant.WebConstants;
import com.musala.soft.drones.controller.medicationController.v1.MedicationController;
import com.musala.soft.drones.model.BaseInfoResource;
import com.musala.soft.drones.model.MedicationDataTransferResource;
import com.musala.soft.drones.model.MedicationResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    public ResponseEntity<MedicationResource> updateMedication(String medicationId, MedicationDataTransferResource medicationDataTransferResource) {
        MedicationResource medicationResource = medicationController.updateMedication(medicationId,
                medicationDataTransferResource);
        return ResponseEntity.ok(medicationResource);
    }

    @Override
    public ResponseEntity<List<MedicationResource>> fetchAllMedications(Integer page, Integer size,
                                                                        String searchText) {
        Pageable pageable = (page != null) ? PageRequest.of(page, size) : null;
        Page<MedicationResource> medicationResources = medicationController.
                fetchMedications(searchText, pageable);
        return ResponseEntity.ok()
                .header(WebConstants.HEADER_KEY_PAGINATION_TOTAL_COUNT, String.valueOf(medicationResources.getTotalElements()))
                .header(WebConstants.HEADER_KEY_PAGINATION_CURRENT_PAGE, String.valueOf(page == null ? 0 : page))
                .header(WebConstants.HEADER_KEY_PAGINATION_HAS_MORE, String.valueOf(medicationResources.hasNext()))
                .body(medicationResources.getContent());
    }

    @Override
    public ResponseEntity<BaseInfoResource> deleteMedication(String medicationId) {
        BaseInfoResource baseInfoResource = medicationController.deleteMedication(medicationId);
        return ResponseEntity.ok(baseInfoResource);
    }
}
