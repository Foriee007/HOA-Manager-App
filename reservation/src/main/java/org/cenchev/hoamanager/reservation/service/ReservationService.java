package org.cenchev.hoamanager.reservation.service;

import org.cenchev.hoamanager.reservation.model.dto.ReservationInitiateDTO;
import org.cenchev.hoamanager.reservation.model.entity.Reservation;

public interface ReservationService {
    Reservation initiateReservation(ReservationInitiateDTO reservationInitiateDTO);

    Reservation approveReservation(Long id);
}
