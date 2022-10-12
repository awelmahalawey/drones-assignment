package com.musala.soft.drones.mapper;

import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.model.MedicationResource;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class MedicationDataMapper {

    private ModelMapper modelMapper;

    @Autowired
    private HttpServletRequest context;

    public MedicationDataMapper() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public MedicationResource map(Medication medication) {
        if(medication == null) {
            return null;
        }
        MedicationResource medicationResource = this.modelMapper.map(medication, MedicationResource.class);
        return medicationResource;
    }
}
