package org.cenchev.hoamanagerapp.services;

import java.time.LocalDate;

public interface ParkAvailabilityService {

    int getMinParkingInventory(Long id, LocalDate startDate, LocalDate endDate);
}
