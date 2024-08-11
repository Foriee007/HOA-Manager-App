package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationByHomeId(Long id);
    Optional<Reservation> findReservationByIdAndResidentId(Long reservationId, Long userId);
    //List<Reservation> findReservationByResidentId(Long id);  //To do

}
