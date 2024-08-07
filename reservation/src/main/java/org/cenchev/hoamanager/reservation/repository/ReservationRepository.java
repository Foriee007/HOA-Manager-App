package org.cenchev.hoamanager.reservation.repository;

import org.cenchev.hoamanager.reservation.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}