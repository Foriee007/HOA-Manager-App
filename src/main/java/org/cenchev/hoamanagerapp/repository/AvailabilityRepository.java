package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Query("SELECT MIN(COALESCE(a.availableSpots, r.parkingCount)) FROM Garage r LEFT JOIN Availability a " +
            "ON a.garage.id = r.id " +
            "AND a.date BETWEEN :startDate AND :endDate WHERE r.id = :id")
    Optional<Integer> getMinAvailableParking(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
