package org.cenchev.hoamanager.reservation.service.impl;

import org.cenchev.hoamanager.reservation.model.dto.GarageDesignatedDTO;
import org.cenchev.hoamanager.reservation.model.dto.ReservationInitiateDTO;
import org.cenchev.hoamanager.reservation.model.entity.GarageDesignated;
import org.cenchev.hoamanager.reservation.model.entity.Reservation;
import org.cenchev.hoamanager.reservation.repository.ReservationRepository;
import org.cenchev.hoamanager.reservation.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation initiateReservation(ReservationInitiateDTO reservationInitiateDTO) {
        Reservation reservation = new Reservation();
        reservation.setHomeId(reservationInitiateDTO.getHomeId());
        reservation.setClientId(reservationInitiateDTO.getClientId());
        reservation.setStartDate(reservationInitiateDTO.getStartDate());
        reservation.setEndDate(reservationInitiateDTO.getEndDate());
        reservation.setDays(reservationInitiateDTO.getDays());
        reservation.setGarageDesignated(convertToEntityList(reservationInitiateDTO.getGarageDesignated()));
        reservation.setTotalPrice(reservationInitiateDTO.getTotalPrice());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation approveReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));
        // Logic to approve reservation
        return reservationRepository.save(reservation);
    }

    private List<GarageDesignated> convertToEntityList(List<GarageDesignatedDTO> dtos) {
        return dtos.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    private GarageDesignated convertToEntity(GarageDesignatedDTO dto) {
        GarageDesignated entity = new GarageDesignated();
        entity.setSpaceType(dto.getSpaceType());
        entity.setCount(dto.getCount());
        return entity;
    }

}
