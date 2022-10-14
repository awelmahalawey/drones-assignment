package com.musala.soft.drones.repository;

import com.musala.soft.drones.entity.DronePayloadItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DronePayloadItemRepository extends JpaRepository<DronePayloadItem, UUID> {
}
