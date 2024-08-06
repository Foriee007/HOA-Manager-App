package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    Optional<Home> findByName(String name);
    List<Home> findAllByPropertyManagerId(Long id);

    Optional<Home> findByIdAndPropertyManager_Id(Long id, Long propertyManagerId);
}
