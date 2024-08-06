package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.entities.PropertyManager;
import org.cenchev.hoamanagerapp.model.entities.User;

public interface PropertyManagerService {
    PropertyManager findByUser(User user);
}
