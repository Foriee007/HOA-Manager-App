package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

}
