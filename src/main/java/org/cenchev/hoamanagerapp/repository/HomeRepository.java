package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.dto.HomeDTO;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    Optional<Home> findByName(String name);
    List<Home> findAllByPropertyManager_Id(Long id);

    Optional<Home> findByIdAndPropertyManager_Id(Long id, Long propertyManagerId);

    @Query("SELECT h FROM Home h WHERE h.address.city = :city AND h.address.state = :state")
    List<Home> findHomesByCityAndState(@Param("city") String city,
                                       @Param("state") String state);

    //criteria min 1 day availability
    //List<Home> findHomesWithAvailableParking(String city, String state, LocalDate startDate, LocalDate endDate, Long numberOfDays);
}
