package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.entities.Resident;

import java.util.Optional;

public interface ResidentService {
    Optional<Resident> findByUserId(Long userId);
}
