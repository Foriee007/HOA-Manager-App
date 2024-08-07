package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.AvailableHomeParkingDTO;
import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.dto.HomeRegistrationDTO;
import org.cenchev.hoamanagerapp.model.entities.Home;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface HomeService {
    Home saveHome(HomeRegistrationDTO homeRegistrationDTO) throws IOException;

    List<HomeDTO> findAllHomesByManagerId(Long managerId);

    HomeDTO mapHomeToHomeDto(Home home);

    void deleteHomeByIdAndManagerId(Long id, Long propertyManagerId);

    HomeDTO findHomeByIdAndPropertyManagerId(Long id, Long propertyManagerId);

    HomeDTO updateHomeByManagerId(HomeDTO homeDTO, Long managerId, String imageUrl);

    List<AvailableHomeParkingDTO> findAvailableHomesByLocationAndDate(String city, String state, LocalDate startDate, LocalDate endDate);

    AvailableHomeParkingDTO findAvailableHomeById(Long id, LocalDate startDate, LocalDate endDate);
}
