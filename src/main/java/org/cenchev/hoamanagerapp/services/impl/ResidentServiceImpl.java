package org.cenchev.hoamanagerapp.services.impl;

import org.cenchev.hoamanagerapp.model.entities.Resident;
import org.cenchev.hoamanagerapp.repository.ResidentRepository;
import org.cenchev.hoamanagerapp.services.ResidentService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResidentServiceImpl implements ResidentService {
    private final ResidentRepository residentRepository;

    public ResidentServiceImpl(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    @Override
    public Optional<Resident> findByUserId(Long userId) {
        return residentRepository.findByUserId(userId);
    }
}
