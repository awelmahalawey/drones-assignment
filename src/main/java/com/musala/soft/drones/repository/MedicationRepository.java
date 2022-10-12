package com.musala.soft.drones.repository;

import com.musala.soft.drones.entity.Medication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    Medication findByNameAndCode(String name, String code);

    Medication findByIdAndIsActive(UUID id, Boolean isActive);

    @Query(value = "SELECT DISTINCT m " +
            "FROM Medication m " +
            "WHERE " +
            "   ( " +
            "       ( " +
            "           (LOWER(m.name) LIKE CONCAT('%', :searchText, '%')) " +
            "           OR (m.code LIKE CONCAT('%', :searchText, '%')) " +
            "       ) " +
            "       AND m.isActive = true " +
            "   )")
    Page<Medication> findAllWithFilters(
            @Param("searchText") String searchText,
            Pageable pageable);
}
