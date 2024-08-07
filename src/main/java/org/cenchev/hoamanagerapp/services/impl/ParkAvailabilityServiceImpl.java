package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.cenchev.hoamanagerapp.repository.AvailabilityRepository;
import org.cenchev.hoamanagerapp.services.GarageService;
import org.cenchev.hoamanagerapp.services.ParkAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class ParkAvailabilityServiceImpl implements ParkAvailabilityService {
    private final GarageService garageService;
    private final AvailabilityRepository availabilityRepository;

    public ParkAvailabilityServiceImpl(GarageService garageService, AvailabilityRepository availabilityRepository) {
        this.garageService = garageService;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    public int getMinParkingInventory(Long id, LocalDate startDate, LocalDate endDate) {
        //Exception for Optional check by ID
        Garage garage = garageService.findGarageById(id).orElseThrow(() -> new EntityNotFoundException("Garage not found"));
        //Bring back what is  minimum available for those range of days. If not match we have full inventory of Garage
        return availabilityRepository.getMinAvailableParking(id, startDate, endDate)
                .orElse(garage.getParkingCount());
    }
}
