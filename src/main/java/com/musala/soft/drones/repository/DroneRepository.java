package com.musala.soft.drones.repository;

import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.enumerator.DroneModel;
import com.musala.soft.drones.enumerator.DroneState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

@Repository
public interface DroneRepository extends JpaRepository<Drone, UUID> {

    Drone findBySerialNumber(String serialNumber);

    Drone findByIdAndIsActive(UUID id, Boolean isActive);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select d from Drone d where d.id = :droneId and d.isActive = :isActive")
    Drone findByIdAndIsActiveWithLock(@Param("droneId") UUID droneId, @Param("isActive") Boolean isActive);

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

    @Query(value = "SELECT DISTINCT d " +
            "FROM Drone d " +
            "WHERE " +
            "   ( " +
            "       d.state = :state" +
            "       AND d.batteryCap >= :batteryCap" +
            "       AND d.weightLimit >= :payloadWeight" +
            "       AND d.isActive = true " +
            "   ) " +
            "ORDER BY d.batteryCap DESC")
    List<Drone> findDronesByStateAndBatteryCapMoreThanAndWeightLimitMoreThan(
            @Param("state") DroneState droneState,
            @Param("batteryCap") Double batteryCap, @Param("payloadWeight") Double payloadWeight);
}
