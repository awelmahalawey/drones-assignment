package com.musala.soft.drones.repository;

import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.Medication;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import com.musala.soft.drones.enumerator.PayloadState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, UUID> {

    Drone findBySerialNumber(String serialNumber);

    Drone findByIdAndIsActive(UUID id, Boolean isActive);

    @Query(value = "SELECT DISTINCT d " +
            "FROM Drone d " +
            "WHERE " +
            "   ( " +
            "       ( " +
            "           (LOWER(d.serialNumber) LIKE CONCAT('%', :searchText, '%')) " +
            "       ) " +
            "       AND ( " +
            "               (:searchByDroneState = false) " +
            "               OR (d.state = :droneState) " +
            "           )" +
            "       AND ( " +
            "               (:searchByDroneModel = false) " +
            "               OR (d.model = :droneModel) " +
            "           )" +
            "       AND d.isActive = true " +
            "   )")
    List<Drone> findAllWithFilters(
            @Param("searchText") String searchText,
            @Param("searchByDroneState") Boolean searchByDroneState,
            @Param("droneState") DroneState droneState,
            @Param("searchByDroneModel") Boolean searchByDroneModel,
            @Param("droneModel") DroneModel droneModel);
}