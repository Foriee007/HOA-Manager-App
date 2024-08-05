package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.dto.HomeRegistrationDTO;
import org.cenchev.hoamanagerapp.model.entities.Home;

import java.io.IOException;
import java.util.List;

public interface HomeService {
    Home saveHome(HomeRegistrationDTO homeRegistrationDTO) throws IOException;

    List<HomeDTO> findAllHomesByManagerId(Long managerId);

    HomeDTO mapHomeToHomeDto(Home home);

    void deleteHomeByIdAndManagerId(Long id, Long propertyManagerId);

    HomeDTO findHomeByIdAndPropertyManagerId(Long id, Long propertyManagerId);

    HomeDTO updateHomeByManagerId(HomeDTO homeDTO, Long managerId, String imageUrl);
}
