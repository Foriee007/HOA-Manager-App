package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByUserId(Long userId);

    @Query("SELECT c FROM Resident c WHERE c.user.username = :username")
    Optional<Resident> findByUsername(@Param("username") String username);
}
