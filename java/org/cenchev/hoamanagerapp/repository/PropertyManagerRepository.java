package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.PropertyManager;
import org.cenchev.hoamanagerapp.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PropertyManagerRepository extends JpaRepository<PropertyManager, Long> {
    Optional<PropertyManager> findByUser(User user);

}
