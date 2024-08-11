package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.GarageRequestDTO;

import java.time.LocalDate;
import java.util.List;

public interface ParkAvailabilityService {

    int getMinParkingInventory(Long id, LocalDate startDate, LocalDate endDate);

    void updateAvailabilities(long homeId, LocalDate startDate, LocalDate endDate, List<GarageRequestDTO> garageRequest);
}
