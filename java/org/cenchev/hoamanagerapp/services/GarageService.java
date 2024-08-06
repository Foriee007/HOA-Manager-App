package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.GarageDTO;
import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.cenchev.hoamanagerapp.model.entities.Home;

import java.util.List;

public interface GarageService {
    Garage saveGarage(GarageDTO garageDTO, Home home);

    List<Garage> saveGarageSpots(List<GarageDTO> garageDTOs, Home home);

    Garage mapGarageDTOToGarageSpot(GarageDTO garageDTO, Home home);

    GarageDTO mapGarageSpotsToGarageDTO(Garage garage);

    Garage updateGarageSpots(GarageDTO garageDTO);
}
