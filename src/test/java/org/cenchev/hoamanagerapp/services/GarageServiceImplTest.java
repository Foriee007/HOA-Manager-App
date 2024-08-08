package org.cenchev.hoamanagerapp.services;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.dto.GarageDTO;
import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.cenchev.hoamanagerapp.model.enums.SpaceType;
import org.cenchev.hoamanagerapp.repository.GarageRepository;
import org.cenchev.hoamanagerapp.services.impl.GarageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private GarageServiceImpl garageService;

    private GarageDTO garageDTO;
    private Garage garage;
    private Home home;

    @BeforeEach
    public void setUp() {
        home = new Home();
        home.setId(1L);

        garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        garageDTO.setHomeId(1L);
        garageDTO.setSpaceType(SpaceType.EV);
        garageDTO.setPricePerDay(20.0);
        garageDTO.setParkingSpotCount(2);

        garage = new Garage();
        garage.setId(1L);
        garage.setHome(home);
        garage.setSpaceType(SpaceType.REGULAR);
        garage.setDayUsePrice(20.0);
        garage.setParkingCount(2);
    }

    @Test
    public void testSaveGarage() {

        when(garageRepository.save(any(Garage.class))).thenReturn(garage);

        //  check if saves the garage correctly.
        Garage result = garageService.saveGarage(garageDTO, home);

        assertEquals(garage, result);
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    public void testSaveGarageSpots() {
        // check if saves multiple garage spots correctly
        List<GarageDTO> garageDTOs = List.of(garageDTO);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);

        List<Garage> result = garageService.saveGarageSpots(garageDTOs, home);

        assertEquals(1, result.size());
        assertEquals(garage, result.get(0));
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    public void testMapGarageDTOToGarageSpot() {
        // Check if  maps the DTO to a garage entity correctly.
        Garage result = garageService.mapGarageDTOToGarageSpot(garageDTO, home);

        assertEquals(home, result.getHome());
        assertEquals(garageDTO.getSpaceType(), result.getSpaceType());
        assertEquals(garageDTO.getPricePerDay(), result.getDayUsePrice());
        assertEquals(garageDTO.getParkingSpotCount(), result.getParkingCount());
    }

    @Test
    public void testMapGarageSpotsToGarageDTO() {
        // method to check it maps the garage entity to a DTO correctly
        GarageDTO result = garageService.mapGarageSpotsToGarageDTO(garage);

        assertEquals(garage.getId(), result.getId());
        assertEquals(home.getId(), result.getHomeId());
        assertEquals(garage.getSpaceType(), result.getSpaceType());
        assertEquals(garage.getDayUsePrice(), result.getPricePerDay());
        assertEquals(garage.getParkingCount(), result.getParkingSpotCount());
    }

    @Test
    public void testUpdateGarageSpots_GarageExists() {
        // method when the garage exists, ensuring it updates the garage correctly.
        when(garageRepository.findById(garageDTO.getId())).thenReturn(Optional.of(garage));
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);

        Garage result = garageService.updateGarageSpots(garageDTO);

        assertEquals(garageDTO.getSpaceType(), result.getSpaceType());
        assertEquals(garageDTO.getParkingSpotCount(), result.getParkingCount());
        assertEquals(garageDTO.getPricePerDay(), result.getDayUsePrice());
        verify(garageRepository, times(1)).findById(garageDTO.getId());
        verify(garageRepository, times(1)).save(any(Garage.class));
    }

    @Test
    public void testUpdateGarageSpots_GarageNotExists() {
        // method when the garage does not exist, check if it throws an EntityNotFoundException.
        when(garageRepository.findById(garageDTO.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            garageService.updateGarageSpots(garageDTO);
        });

        assertEquals("Parking Id  not found", exception.getMessage());
        verify(garageRepository, times(1)).findById(garageDTO.getId());
        verify(garageRepository, never()).save(any(Garage.class));
    }

    @Test
    public void testFindGarageById_GarageExists() {
        // method when the garage exists, check if  it finds the garage correctly
        when(garageRepository.findById(garage.getId())).thenReturn(Optional.of(garage));

        Optional<Garage> result = garageService.findGarageById(garage.getId());

        assertTrue(result.isPresent());
        assertEquals(garage, result.get());
    }

    @Test
    public void testFindGarageById_GarageNotExists() {
        // method when the garage does not exist, check if it handles the absence of the garage correctly
        when(garageRepository.findById(garage.getId())).thenReturn(Optional.empty());

        Optional<Garage> result = garageService.findGarageById(garage.getId());

        assertFalse(result.isPresent());
    }
}
