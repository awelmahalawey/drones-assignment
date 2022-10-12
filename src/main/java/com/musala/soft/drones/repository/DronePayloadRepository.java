package com.musala.soft.drones.repository;

import com.musala.soft.drones.entity.Drone;
import com.musala.soft.drones.entity.DronePayload;
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
public interface DronePayloadRepository extends JpaRepository<DronePayload, UUID> {

    DronePayload findByIdAndIsActive(UUID id, Boolean isActive);

    @Query("SELECT DISTINCT dp " +
            "FROM DronePayload dp " +
            "Where " +
            "   ( " +
            "       dp.drone = :drone " +
            "       AND ( " +
            "               (:searchByPayloadState = false) " +
            "               OR (dp.state = :payloadState) " +
            "           )" +
            "       AND dp.isActive = true " +
            "   )")
    List<DronePayload> findAllWithFilter(
            @Param("drone") Drone drone,
            @Param("searchByPayloadState") Boolean searchByPayloadState,
            @Param("payloadState") PayloadState payloadState);
}
