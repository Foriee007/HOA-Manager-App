package org.cenchev.hoamanagerapp.services;

import org.cenchev.hoamanagerapp.model.dto.ReservationDTO;
import org.cenchev.hoamanagerapp.model.dto.ReservationRequestDTO;
import org.cenchev.hoamanagerapp.model.entities.Home;
import org.cenchev.hoamanagerapp.model.entities.Reservation;
import org.cenchev.hoamanagerapp.model.entities.Resident;

import java.util.List;

public interface ReservationService {
    Reservation saveRequestReservation(ReservationRequestDTO reservationInitiationDTO, Long residentId);

    ReservationDTO confirmReservation(ReservationRequestDTO reservationRequestDTO, Long userId);
    Reservation mapReservationRequestDTOToReservation(ReservationRequestDTO reservationRequestDTO, Resident resident, Home home);
    ReservationDTO mapReservationToReservationDto(Reservation reservation);

    List<ReservationDTO> findReservationByManagerId(Long managerId);
}
