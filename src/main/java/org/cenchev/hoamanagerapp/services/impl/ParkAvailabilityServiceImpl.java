package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.cenchev.hoamanagerapp.model.dto.GarageRequestDTO;
import org.cenchev.hoamanagerapp.model.entities.Availability;
import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;
import org.cenchev.hoamanagerapp.repository.ParkAvailabilityRepository;
import org.cenchev.hoamanagerapp.services.GarageService;
import org.cenchev.hoamanagerapp.services.HomeService;
import org.cenchev.hoamanagerapp.services.ParkAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkAvailabilityServiceImpl implements ParkAvailabilityService {
    private final GarageService garageService;
    private final ParkAvailabilityRepository parkAvailabilityRepository;
    private final HomeService homeService;

    public ParkAvailabilityServiceImpl(GarageService garageService, ParkAvailabilityRepository parkAvailabilityRepository, HomeService homeService) {
        this.garageService = garageService;
        this.parkAvailabilityRepository = parkAvailabilityRepository;
        this.homeService = homeService;
    }

    @Override
    public int getMinParkingInventory(Long id, LocalDate startDate, LocalDate endDate) {
        //Exception for Optional check by ID
        Garage garage = garageService.findGarageById(id).orElseThrow(() -> new EntityNotFoundException("Garage not found"));
        //Bring back what is  minimum available for those range of days. If not match we have full inventory of Garage
        return parkAvailabilityRepository.getMinAvailableParking(id, startDate, endDate)
                .orElse(garage.getParkingCount());
    }

    @Override
    @Transactional
    public void updateAvailabilities(long homeId, LocalDate startDate, LocalDate endDate, List<GarageRequestDTO> garageRequests) {
        Home home = homeService.findHomeById(homeId).orElseThrow(() -> new EntityNotFoundException("Home not found"));

        garageRequests = garageRequests.stream()
                        .filter(request -> request.getCount() > 0)
                        .collect(Collectors.toList());

        /*List<GarageRequestDTO> list = new ArrayList<>();
        for (GarageRequestDTO request : garageRequests) {
            if (request.getCount() > 0) {
                list.add(request);
            }
        }
        garageRequests = list;*/

        // Iterate through the parking  selections made by the user
        for (GarageRequestDTO garageRequest : garageRequests) {
            SpaceType spaceType = garageRequest.getSpaceType();
            int selectedCount = garageRequest.getCount();

            Garage garage = home.getSpot().stream()
                    .filter(r -> r.getSpaceType() == spaceType)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Space type not found"));

            // Iterate through the dates and update or create availability
            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                final LocalDate currentDate = date; // Temporary final variable
                Availability availability = parkAvailabilityRepository.findByGarageIdAndDate(garage.getId(), date)
                        .orElseGet(() -> {
                            Availability newAvailability = new Availability();
                            newAvailability.setHome(home);
                            newAvailability.setDate(currentDate);
                            newAvailability.setParking(garage);
                            newAvailability.setAvailableSpots(garage.getParkingCount());
                            return newAvailability;
                        });

                // Reduce the available spots by the selected count
                int updatedAvailableParkingSpots = availability.getAvailableSpots() - selectedCount;
                if (updatedAvailableParkingSpots < 0) {
                    throw new IllegalArgumentException("Selected parking spots exceed available spots for date: " + currentDate);
                }
                availability.setAvailableSpots(updatedAvailableParkingSpots);

                parkAvailabilityRepository.save(availability);
            }
        }
    }
    }
