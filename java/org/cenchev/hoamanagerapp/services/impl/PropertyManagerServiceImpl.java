package org.cenchev.hoamanagerapp.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.cenchev.hoamanagerapp.model.entities.PropertyManager;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.cenchev.hoamanagerapp.repository.PropertyManagerRepository;
import org.cenchev.hoamanagerapp.services.PropertyManagerService;
import org.springframework.stereotype.Service;

@Service
public class PropertyManagerServiceImpl implements PropertyManagerService {
    private final PropertyManagerRepository propertyManagerRepository;

    public PropertyManagerServiceImpl(PropertyManagerRepository propertyManagerRepository) {
        this.propertyManagerRepository = propertyManagerRepository;
    }

    @Override
    public PropertyManager findByUser(User user) {
        return propertyManagerRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("PropertyManager account not found" + user.getUsername()));
    }
}
