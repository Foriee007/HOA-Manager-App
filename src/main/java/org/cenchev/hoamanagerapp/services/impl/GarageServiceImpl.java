package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.dto.GarageDTO;
import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.cenchev.hoamanagerapp.repository.GarageRepository;
import org.cenchev.hoamanagerapp.services.GarageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GarageServiceImpl implements GarageService {
    private final GarageRepository garageRepository;

    public GarageServiceImpl(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    @Override
    public Garage saveGarage(GarageDTO garageDTO, Home home) {
        Garage garage = mapGarageDTOToGarageSpot(garageDTO,home);
        //Successfully saved  (day_use_price, parking_count, space_type, home_id)
        garage = garageRepository.save(garage);
        return garage;
    }

    @Override
    public List<Garage> saveGarageSpots(List<GarageDTO> garageDTOs, Home home) {  //referred  HoseService
        List<Garage> garageSpots = garageDTOs.stream()
                .map(garageDTO -> saveGarage(garageDTO,home))
                .collect(Collectors.toList());
        return garageSpots;
    }

    @Override
    public Garage mapGarageDTOToGarageSpot(GarageDTO garageDTO, Home home) {
        Garage garage = new Garage();
        garage.setHome(home);
        garage.setSpaceType(garageDTO.getSpaceType());
        garage.setDayUsePrice(garageDTO.getPricePerDay());
        garage.setParkingCount(garageDTO.getParkingSpotCount());
        //To Do... move Photo url to Home entity
        //var photo = createImage(garageDTO.getGaragePhotoUrl());
        //garage.setImage(createImage(garageDTO.getGaragePhotoUrl()));
        //imageRepository.saveAndFlush(photo);
        //garage.setImage(photo);
        return garage;
    }

    @Override
    public GarageDTO mapGarageSpotsToGarageDTO(Garage garage) {
        GarageDTO garageDTOs = new GarageDTO();
        garageDTOs.setId(garage.getId());
        garageDTOs.setHomeId(garage.getHome().getId());
        garageDTOs.setSpaceType(garage.getSpaceType());
        garageDTOs.setPricePerDay(garage.getDayUsePrice());
        garageDTOs.setParkingSpotCount(garage.getParkingCount());
        return garageDTOs;
    }

    @Override
    public Garage updateGarageSpots(GarageDTO garageDTO) {
        Garage getExistingGarageSpot = garageRepository.findById(garageDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Parking Id  not found"));
        getExistingGarageSpot.setSpaceType(garageDTO.getSpaceType());
        getExistingGarageSpot.setParkingCount(garageDTO.getParkingSpotCount());
        getExistingGarageSpot.setDayUsePrice(garageDTO.getPricePerDay());
        return garageRepository.save(getExistingGarageSpot);
    }
}
